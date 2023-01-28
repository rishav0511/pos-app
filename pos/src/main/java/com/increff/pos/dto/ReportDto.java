package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.TimeUtil;
import com.increff.pos.util.ValidationUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReportDto {

    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailySalesReportService dailySalesReportService;

    public List<InventoryReportData> getInventoryReport() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = new ArrayList<InventoryReportData>();
        List<BrandCategoryPojo> brandPojoList = brandCategoryService.selectAll();
        for(BrandCategoryPojo pojo : brandPojoList) {
            int totalQuantity = getProductQuantityForBrandCategory(pojo.getId());
            InventoryReportData inventoryReportData = new InventoryReportData(pojo.getBrand(), pojo.getCategory(), totalQuantity);
            inventoryReportDataList.add(inventoryReportData);
        }
        return inventoryReportDataList;
    }

    private Integer getProductQuantityForBrandCategory(Integer brandId) throws ApiException {
        Integer totalQuantity = 0;
        List<ProductPojo> productPojoList = productService.getProductByBrandCategory(brandId);
        for(ProductPojo product : productPojoList) {
            InventoryPojo inventory = inventoryService.getInventory(product.getId());
            totalQuantity += inventory.getQuantity();
        }
        return totalQuantity;
    }

    public List<BrandCategoryData> getBrandCategoryReport() {
        List<BrandCategoryPojo> pojos = brandCategoryService.selectAll();
        return pojos.stream().map(ConvertUtil::convertPojotoData).collect(Collectors.toList());
    }

    public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
        Date startingDate = form.getStartDate();
        Date endingDate = form.getEndDate();
        List<OrderPojo> orderList = orderService.getAllBetween(startingDate, endingDate);
        List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectAlikeBrandCategory(form.getBrand(),form.getCategory());
        List<OrderItemPojo> orderItemList = new ArrayList<OrderItemPojo>();
        for (OrderPojo order : orderList) {
            List<OrderItemPojo> orderItemListTemp = orderItemService.selectByOrderId(order.getId());
            orderItemList.addAll(orderItemListTemp);
        }
        List<SalesReportData> salesReportData = getReport(orderItemList,brandCategoryList);
        return salesReportData;
    }

    public List<SalesReportData> getReport(List<OrderItemPojo> orderItemList, List<BrandCategoryPojo> brandCategoryList) throws ApiException {
        Map<Integer,SalesReportData> salesReportDataMap = initializeBrandSalesMap(brandCategoryList);
        for(OrderItemPojo orderItemPojo:orderItemList){
            ProductPojo productPojo = productService.getProduct(orderItemPojo.getProductId());
            if(salesReportDataMap.containsKey(productPojo.getBrandId())) {
                SalesReportData salesReportData = salesReportDataMap.get(productPojo.getBrandId());
                salesReportData.setQuantity(salesReportData.getQuantity() + orderItemPojo.getQuantity());
                salesReportData.setRevenue(salesReportData.getRevenue() + orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                salesReportDataMap.put(productPojo.getBrandId(), salesReportData);
            }
        }
        return ConvertUtil.getSalesReportData(salesReportDataMap);
    }

    private Map<Integer,SalesReportData> initializeBrandSalesMap(List<BrandCategoryPojo> brandCategoryPojos){
        Map<Integer,SalesReportData> brandSalesMapping = new HashMap<>();
        for(BrandCategoryPojo brandCategoryPojo:brandCategoryPojos){
            SalesReportData salesReportData = new SalesReportData(brandCategoryPojo.getBrand(),brandCategoryPojo.getCategory(),(Integer) 0,(Double) 0.0);
            brandSalesMapping.put(brandCategoryPojo.getId(),salesReportData);
        }
        return brandSalesMapping;
    }

    public List<DailySalesReportData> getDailySalesReport() {
        List<DailySalesReportData> dailySalesReportDataList = new ArrayList<DailySalesReportData>();
        List<DailySalesReportPojo> dailySalesReportPojoList = dailySalesReportService.getAll();
        for(DailySalesReportPojo dailySalesReportPojo : dailySalesReportPojoList) {
            dailySalesReportDataList.add(ConvertUtil.getDailySalesReportData(dailySalesReportPojo));
        }
        return dailySalesReportDataList;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() throws ApiException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = TimeUtil.getStartOfDay(cal.getTime());
        Date present = new Date();

        List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, present);
        int itemsSold = 0;
        double revenue = 0;
        for (OrderPojo order : orderPojoList) {
            List<OrderItemPojo> orderItemList = orderItemService.selectByOrderId(order.getId());
            for(OrderItemPojo orderItem: orderItemList) {
                revenue = revenue + orderItem.getQuantity() * orderItem.getSellingPrice();
            }
            itemsSold += orderItemList.size();
        }
        DailySalesReportPojo dailySalesReportPojo = ConvertUtil
                .setDailySalesReportPojo(new Date(),itemsSold,orderPojoList.size(),revenue);
        dailySalesReportService.insert(dailySalesReportPojo);
    }
}

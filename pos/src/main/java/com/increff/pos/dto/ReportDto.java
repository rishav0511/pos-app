package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.TimeUtil;
import com.increff.pos.util.ValidationUtils;
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
//        ValidationUtils.validateForm(form);
//        NormalizeUtil.normalizePojo(form);
        Date startingDate = form.getStartDate();
        Date endingDate = form.getEndDate();
        List<OrderPojo> orderList = orderService.getAllBetween(startingDate, endingDate);
//         new ArrayList<>();
        // Brand-Category is empty
//        List<BrandCategoryPojo> brandCategoryList;
//        if(form.getBrand().equals("") && form.getCategory().equals("")) {
//            brandCategoryList = brandCategoryService.selectAll();
//        }
//        //Category is empty
//        else if (form.getBrand()!=null && form.getCategory().equals("")) {
//            brandCategoryList = brandCategoryService.selectByBrand(form.getBrand());
//        } else if(form.getBrand().equals("") && form.getCategory()!=null) {
//            brandCategoryList = brandCategoryService.selectByCategory(form.getCategory());
//        }
//        else {
//            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(form.getBrand(),form.getCategory());
//            brandCategoryList.add(brandCategoryPojo);
//        }
        List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectAlikeBrandCategory(form.getBrand(),form.getCategory());
        List<SalesReportData> salesReportData = getReport(orderList,brandCategoryList);
        return salesReportData;
    }

    public List<SalesReportData> getReport(List<OrderPojo> orderPojoList, List<BrandCategoryPojo> brandCategoryList) throws ApiException {
        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        List<OrderItemPojo> orderItemList = new ArrayList<OrderItemPojo>();
        // Get all order items of all orders
        for (OrderPojo order : orderPojoList) {
            List<OrderItemPojo> orderItemListTemp = orderItemService.selectByOrderId(order.getId());
            orderItemList.addAll(orderItemListTemp);
        }
        for(BrandCategoryPojo brandCategoryPojo:brandCategoryList){
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setCategory(brandCategoryPojo.getCategory());
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            Integer quantity = 0;
            Double revenue = 0.0;
            for(OrderItemPojo orderItemPojo:orderItemList){
                ProductPojo productPojo = productService.getProduct(orderItemPojo.getProductId());
                if(productPojo.getBrandId()==brandCategoryPojo.getId()){
                    quantity += orderItemPojo.getQuantity();
                    revenue += (orderItemPojo.getQuantity())*(orderItemPojo.getSellingPrice());
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(revenue);
            salesReportDataList.add(salesReportData);
        }
        return salesReportDataList;
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
    public void generateDailySalesReport() {
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

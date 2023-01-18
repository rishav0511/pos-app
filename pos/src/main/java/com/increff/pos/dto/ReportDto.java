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

    public List<InventoryReportData> getInventoryReport() {
        List<InventoryReportData> inventoryReportData = new ArrayList();
        List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectAll();
        for (BrandCategoryPojo brandCategory : brandCategoryList) {
            InventoryReportData inventoryReportItemDataItem = new InventoryReportData();
            inventoryReportItemDataItem.setBrand(brandCategory.getBrand());
            inventoryReportItemDataItem.setCategory(brandCategory.getCategory());
            inventoryReportItemDataItem.setId(brandCategory.getId());
            int quantity = 0;
            List<ProductPojo> productList = productService.getProductByBrandCategory(brandCategory.getId());
            for (ProductPojo pojo : productList) {
                InventoryPojo inventoryPojo = inventoryService.get(pojo.getId());
                quantity+=inventoryPojo.getQuantity();
            }
            inventoryReportItemDataItem.setQuantity(quantity);
            inventoryReportData.add(inventoryReportItemDataItem);
        }
        return inventoryReportData;
    }

    public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
        ValidationUtils.validateForm(form);
        NormalizeUtil.normalizeForm(form);
        Date startingDate = form.getStartDate();
        Date endingDate = form.getEndDate();
        List<OrderPojo> orderList = orderService.getAllBetween(startingDate, endingDate);
        // Brand-Category is empty
        if(form.getBrand().equals("") && form.getCategory().equals("")) {
            List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectAll();
            List<SalesReportData> salesReportData = getReport(orderList,brandCategoryList);
            return salesReportData;
        }
        //Category is empty
        else if (form.getBrand()!=null && form.getCategory().equals("")) {
            List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectByBrand(form.getBrand());
            List<SalesReportData> salesReportData = getReport(orderList,brandCategoryList);
            return salesReportData;
        } else if(form.getBrand().equals("") && form.getCategory()!=null) {
            List<BrandCategoryPojo> brandCategoryList = brandCategoryService.selectByCategory(form.getCategory());
            List<SalesReportData> salesReportData = getReport(orderList,brandCategoryList);
            return salesReportData;
        }
        else {
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(form.getBrand(),form.getCategory());
            List<BrandCategoryPojo> brandCategoryList = new ArrayList<>();
            brandCategoryList.add(brandCategoryPojo);
            List<SalesReportData> salesReportData = getReport(orderList,brandCategoryList);
            return salesReportData;
        }
    }

    public List<SalesReportData> getReport(List<OrderPojo> orderPojoList, List<BrandCategoryPojo> brandCategoryList) throws ApiException {
        List<SalesReportData> salesReportData = new ArrayList<SalesReportData>();
        List<OrderItemPojo> orderItemList = new ArrayList<OrderItemPojo>();
        // Get all order items of all orders
        for (OrderPojo order : orderPojoList) {
            List<OrderItemPojo> orderItemListTemp = orderItemService.selectByOrderId(order.getId());
            orderItemList.addAll(orderItemListTemp);
        }

        // Get all product of all order items
        List<ProductPojo> productList = new ArrayList<ProductPojo>();
        for (OrderItemPojo orderItem : orderItemList) {
            ProductPojo product = productService.get(orderItem.getProductId());
            productList.add(product);
        }

        // Initialize salesReportData
        for (BrandCategoryPojo brandCategory : brandCategoryList) {
            SalesReportData salesReportItemDataItem = new SalesReportData(brandCategory, 0, 0.00);
            salesReportData.add(salesReportItemDataItem);
        }

        // Calculate salesReportData
        for (OrderItemPojo orderItem : orderItemList) {
            int productId = orderItem.getProductId();
            ProductPojo product = productList.stream().filter(p -> p.getId() == productId).findFirst().get();
            int brandCategoryId = product.getBrandId();
            // Find and update salesReportData
            for (SalesReportData salesReportItemDataItem : salesReportData) {
                if (salesReportItemDataItem.getBrandCategoryId() == brandCategoryId) {
                    salesReportItemDataItem
                            .setQuantity(salesReportItemDataItem.getQuantity() + orderItem.getQuantity());
                    salesReportItemDataItem.setRevenue(
                            salesReportItemDataItem.getRevenue()
                                    + orderItem.getQuantity() * orderItem.getSellingPrice());
                }
            }
        }
        return salesReportData;
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
        DailySalesReportPojo dailySalesReportPojo = new DailySalesReportPojo();
        dailySalesReportPojo.setDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = TimeUtil.getStartOfDay(cal.getTime());
        Date present = new Date();
        System.out.println("From: " + yesterday.toString() + "\nTo: " + present.toString());

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
        dailySalesReportPojo.setInvoiced_orders_count(orderPojoList.size());
        dailySalesReportPojo.setInvoiced_items_count(itemsSold);
        dailySalesReportPojo.setTotal_revenue(revenue);
        dailySalesReportService.insert(dailySalesReportPojo);
    }
}

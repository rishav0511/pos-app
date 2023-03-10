package com.increff.pos.util;

import com.increff.pos.model.*;

import com.increff.pos.pojo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConvertUtil {
    public static BrandCategoryPojo convertFormtoPojo(BrandCategoryForm brandCategoryForm) {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setBrand(brandCategoryForm.getBrand());
        brandCategoryPojo.setCategory(brandCategoryForm.getCategory());
        return brandCategoryPojo;
    }

    public static BrandCategoryData convertPojotoData(BrandCategoryPojo brandCategoryPojo) {
        BrandCategoryData brandData = new BrandCategoryData();
        brandData.setBrand(brandCategoryPojo.getBrand());
        brandData.setCategory(brandCategoryPojo.getCategory());
        brandData.setId(brandCategoryPojo.getId());
        return brandData;
    }

    public static BrandCategoryForm convertProductFormtoBrandForm(ProductForm productForm){
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand(productForm.getBName());
        brandCategoryForm.setCategory(productForm.getBCategory());
        return brandCategoryForm;
    }

    public static ProductPojo convertFormtoPojo(ProductForm productForm, BrandCategoryPojo brandCategoryPojo) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setProduct(productForm.getProduct());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setBrandId(brandCategoryPojo.getId());
        return productPojo;
    }

    public static ProductData convertPojotoData(ProductPojo productPojo, BrandCategoryPojo brandCategoryPojo) {
        ProductData productData = new ProductData();
        productData.setProduct(productPojo.getProduct());
        productData.setBarcode(productPojo.getBarcode());
        productData.setBCategory(brandCategoryPojo.getCategory());
        productData.setBName(brandCategoryPojo.getBrand());
        productData.setMrp(productPojo.getMrp());
        productData.setId(productPojo.getId());
        return  productData;
    }

    public static InventoryData convertPojotoData(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setPName(productPojo.getProduct());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static OrderItemPojo convertFormToPojo(OrderItemForm form, int orderId, int productId) {
        OrderItemPojo pojo = new OrderItemPojo();
        pojo.setQuantity(form.getQuantity());
        pojo.setSellingPrice(form.getSellingPrice());
        pojo.setOrderId(orderId);
        pojo.setProductId(productId);
        return pojo;
    }

    public static OrderItemData convertPojoToData(OrderItemPojo pojo, ProductPojo productPojo) {
        OrderItemData data = new OrderItemData();
        data.setQuantity(pojo.getQuantity());
        data.setSellingPrice(pojo.getSellingPrice());
        data.setBarcode(productPojo.getBarcode());
        data.setProduct(productPojo.getProduct());
        return data;
    }

    public static OrderData convertPojoToData(OrderPojo pojo, List<OrderItemPojo> orderItemPojos) {
        OrderData orderData = new OrderData();
        orderData.setCreatedAt(pojo.getCreatedAt());
        orderData.setOrderId(pojo.getId());
        Double billAmount = (double) 0;
        for(OrderItemPojo orderItemPojo:orderItemPojos) {
            billAmount+=orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
        }
        orderData.setBillAmount(billAmount);
        return orderData;
    }

    public static UserPojo convertFormToPojo(UserForm userForm) {
        UserPojo user = new UserPojo();
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        return user;
    }

    public static UserData convertPojoToData(UserPojo user) {
        UserData data = new UserData();
        data.setEmail(user.getEmail());
        data.setRole(user.getRole());
        data.setId(user.getId());
        return data;
    }


    public static InvoiceData generateInvoice(OrderData orderData, List<OrderItemData> orderItemDataList) {
        InvoiceData data = new InvoiceData();
        data.setOrderData(orderData);
        data.setOrderItemDataList(orderItemDataList);
        return data;
    }


    public static DailySalesReportPojo setDailySalesReportPojo(Date date, int invoiced_items_count, int invoiced_orders_count, double total_revenue) {
        DailySalesReportPojo dailySalesReportPojo = new DailySalesReportPojo();
        dailySalesReportPojo.setDate(date);
        dailySalesReportPojo.setInvoiced_orders_count(invoiced_orders_count);
        dailySalesReportPojo.setInvoiced_items_count(invoiced_items_count);
        dailySalesReportPojo.setTotal_revenue(total_revenue);
        return dailySalesReportPojo;
    }

    public static DailySalesReportData getDailySalesReportData(DailySalesReportPojo dailySalesReportPojo) {
        DailySalesReportData dailySalesReportData = new DailySalesReportData();
        dailySalesReportData.setDate(dailySalesReportPojo.getDate());
        dailySalesReportData.setOrderCount(dailySalesReportPojo.getInvoiced_orders_count());
        dailySalesReportData.setItemCount(dailySalesReportPojo.getInvoiced_items_count());
        dailySalesReportData.setTotalRevenue(dailySalesReportPojo.getTotal_revenue());
        return dailySalesReportData;
    }

    public static List<SalesReportData> getSalesReportData(Map<Integer,SalesReportData> salesReportDataMap) {
        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        for (Map.Entry<Integer, SalesReportData> pair : salesReportDataMap.entrySet()) {
            salesReportDataList.add(pair.getValue());
        }
        return salesReportDataList;
    }
}

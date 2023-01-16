package com.increff.pos.util;

import com.increff.pos.model.*;

import com.increff.pos.pojo.*;

import java.util.List;

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
        orderData.setIsInvoiceCreated(false);
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

    public static DailySalesReportData getDailySalesReportData(DailySalesReportPojo dailySalesReportPojo) {
        DailySalesReportData dailySalesReportData = new DailySalesReportData();
        dailySalesReportData.setDate(dailySalesReportPojo.getDate());
        dailySalesReportData.setOrderCount(dailySalesReportPojo.getInvoiced_orders_count());
        dailySalesReportData.setItemCount(dailySalesReportPojo.getInvoiced_items_count());
        dailySalesReportData.setTotalRevenue(dailySalesReportPojo.getTotal_revenue());
        return dailySalesReportData;
    }
}

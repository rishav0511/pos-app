package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;

import java.util.List;

public class NormalizeUtil {

    public static void normalizeForm(BrandCategoryForm b) {
        b.setBrand(StringUtil.toLowerCase(b.getBrand()));
        b.setCategory(StringUtil.toLowerCase(b.getCategory()));
    }

    public static void normalizeForm(ProductForm p) {
        p.setBName(StringUtil.toLowerCase(p.getBName()));
        p.setBCategory(StringUtil.toLowerCase(p.getBCategory()));
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setProduct(StringUtil.toLowerCase(p.getProduct()));
    }

    public static void normalizeForm(InventoryForm inventoryForm) {
        inventoryForm.setBarcode(StringUtil.toLowerCase(inventoryForm.getBarcode()));
    }

    public static void normalizeForm(List<OrderItemForm> orderItemForms) {
        for(OrderItemForm form:orderItemForms) {
            form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
        }
    }

    public static void normalizeForm(SalesReportForm salesReportForm) {
        salesReportForm.setBrand(StringUtil.toLowerCase(salesReportForm.getBrand()));
        salesReportForm.setCategory(StringUtil.toLowerCase(salesReportForm.getCategory()));
    }

    public static void normalizeUser(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
    }
}

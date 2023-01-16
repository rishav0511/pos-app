package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;

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

    public static void normalizeUser(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
    }
}

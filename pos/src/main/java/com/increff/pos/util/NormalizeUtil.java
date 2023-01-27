package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;

import java.util.List;

public class NormalizeUtil {

    public static void normalizePojo(BrandCategoryPojo b) {
        b.setBrand(StringUtil.toLowerCase(b.getBrand()));
        b.setCategory(StringUtil.toLowerCase(b.getCategory()));
    }

    public static void normalizePojo(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setProduct(StringUtil.toLowerCase(p.getProduct()));
    }

    public static void normalizeUser(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
    }

}

package com.increff.pos.util;

import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;

public class NormalizeUtil {

    public static void normalizePojo(BrandCategoryPojo b) {
        b.setBrand(StringUtil.toLowerCase(b.getBrand()));
        b.setCategory(StringUtil.toLowerCase(b.getCategory()));
    }

    public static void normalizePojo(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setProduct(StringUtil.toLowerCase(p.getProduct()));
        p.setMrp(normalize(p.getMrp()));
    }

    public static void normalizeUser(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
    }

    public static Double normalize(Double input) {
        String[] parts = input.toString().split("\\.");
        if (parts.length == 1) return input;

        String integerPart = parts[0];
        String decimalPart = parts[1];

        if (decimalPart.length() <= 2) return input;
        decimalPart = decimalPart.substring(0, 2);

        String doubleStr = integerPart + "." + decimalPart;
        return Double.parseDouble(doubleStr);
    }

}

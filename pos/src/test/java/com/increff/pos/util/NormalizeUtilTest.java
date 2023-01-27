package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalizeUtilTest {
    @Test
    public void normalizeBrandCategoryPojoTest() {
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo(" AMUL  ", " dAIRY ");
        NormalizeUtil.normalizePojo(brandCategoryPojo);
        assertEquals("amul",brandCategoryPojo.getBrand());
        assertEquals("dairy",brandCategoryPojo.getCategory());
    }

    @Test
    public void normalizeProductPojoTest() {
        ProductPojo productPojo = TestUtils
                .getProductpojo("  AM111 ","half LITRE miLK  ",69.98765,1);
        NormalizeUtil.normalizePojo(productPojo);
        assertEquals("am111",productPojo.getBarcode());
        assertEquals("half litre milk",productPojo.getProduct());
        assertEquals((Double)69.98765,productPojo.getMrp());
    }

    @Test
    public void normalizeUserTest() {
        UserPojo userPojo = TestUtils.getUserPojo("XYZ@XYZ.com","xyz","operator");
        NormalizeUtil.normalizeUser(userPojo);
        assertEquals("xyz@xyz.com",userPojo.getEmail());
        assertEquals("xyz",userPojo.getPassword());
        assertEquals("operator",userPojo.getRole());
    }
}

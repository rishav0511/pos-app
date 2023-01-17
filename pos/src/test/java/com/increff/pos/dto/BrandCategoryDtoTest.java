package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.List;

public class BrandCategoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private BrandCategoryDto brandCategoryDto;

    @Test
    public void addBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        BrandCategoryPojo pojo = brandCategoryDto.addBrand(brandCategoryForm);
        BrandCategoryData data = brandCategoryDto.getBrand(pojo.getId());
        assertEquals("amul",data.getBrand());
        assertEquals("dairy",data.getCategory());
    }

    @Test
    public void getAllBrandCategoryTest() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        BrandCategoryForm secondBrandCategoryForm = TestUtils.getBrandCategoryForm("   nike  ","  footwear  ");
        brandCategoryDto.addBrand(secondBrandCategoryForm);
        List<BrandCategoryData> data = brandCategoryDto.getAllBrand();
        assertEquals(2,data.size());
    }

    @Test
    public void updateBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        BrandCategoryPojo pojo = brandCategoryDto.addBrand(brandCategoryForm);
        BrandCategoryForm updatedBrandCategoryForm = TestUtils.getBrandCategoryForm(" Sudha  ","  Dairy ");
        brandCategoryDto.update(pojo.getId(), updatedBrandCategoryForm);
        BrandCategoryData data = brandCategoryDto.getBrand(pojo.getId());
        assertEquals("sudha",data.getBrand());
        assertEquals("dairy",data.getCategory());
    }

    @Test(expected = ApiException.class)
    public void invalidAddBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null,"  Dairy  ");
        BrandCategoryPojo pojo = brandCategoryDto.addBrand(brandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No brand and category provided");
    }

    @Test(expected = ApiException.class)
    public void brandCategoryAlreadyExistsTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(brandCategoryForm);
        BrandCategoryForm newBrandCategoryForm = TestUtils.getBrandCategoryForm("Amul","Dairy");
        brandCategoryDto.addBrand(newBrandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand and Category already exists");
    }

    @Test(expected = PersistenceException.class)
    public void searchInvalidIdTest() throws ApiException {
        brandCategoryDto.getBrand(1);
        exceptionRule.expect(PersistenceException.class);
    }
}

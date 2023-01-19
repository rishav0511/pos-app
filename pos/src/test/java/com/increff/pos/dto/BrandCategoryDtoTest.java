package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.PersistenceException;
import java.util.List;

public class BrandCategoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private BrandCategoryDto brandCategoryDto;

    /**
     * Add a new Brand Category test
     * @throws ApiException
     */
    @Test
    public void addBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        BrandCategoryData data = brandCategoryDto.addBrand(brandCategoryForm);
        assertNotNull(data.getId());
        assertEquals("amul",data.getBrand());
        assertEquals("dairy",data.getCategory());
    }

    /**
     * Add a duplicate Brand Category test i.e. Brand Category already exists
     * @throws ApiException
     */
    @Test
    public void addDuplicateBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(brandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand and Category already exists");
        BrandCategoryForm newBrandCategoryForm = TestUtils.getBrandCategoryForm("Amul", "Dairy");
        brandCategoryDto.addBrand(newBrandCategoryForm);
    }

    /**
     * Add a null Brand Category test
     * @throws ApiException
     */
    @Test
    public void addNullBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null,"  Dairy  ");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No brand and category provided");
        BrandCategoryData data = brandCategoryDto.addBrand(brandCategoryForm);
    }

    /**
     * Fetch all BrandCategoryData test
     * @throws ApiException
     */
    @Test
    public void getAllBrandCategoryTest() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        BrandCategoryForm secondBrandCategoryForm = TestUtils.getBrandCategoryForm("   nike  ","  footwear  ");
        brandCategoryDto.addBrand(secondBrandCategoryForm);
        List<BrandCategoryData> data = brandCategoryDto.getAllBrand();
        assertEquals(2,data.size());
    }

    /**
     * Update a Brand Category test
     * @throws ApiException
     */
    @Test
    public void updateBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        BrandCategoryData data = brandCategoryDto.addBrand(brandCategoryForm);
        BrandCategoryForm updatedBrandCategoryForm = TestUtils.getBrandCategoryForm(" Sudha  ","  Dairy ");
        brandCategoryDto.update(data.getId(), updatedBrandCategoryForm);
        BrandCategoryData updatedData = brandCategoryDto.getBrand(data.getId());
        assertEquals("sudha",updatedData.getBrand());
        assertEquals("dairy",updatedData.getCategory());
    }

    /**
     * Fetching an invalid BrandId test
     * @throws ApiException
     */
    @Test
    public void searchInvalidIdTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        BrandCategoryData data = brandCategoryDto.addBrand(brandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand with given id doesn't exist, id:5");
        BrandCategoryData invalidData = brandCategoryDto.getBrand(5);
    }

    /**
     * Adding blank Brand Category test
     * @throws ApiException
     */
    @Test
    public void addBlankBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("    ","    ");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No brand and category provided");
        brandCategoryDto.addBrand(brandCategoryForm);
    }
}

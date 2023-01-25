package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandCategoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Autowired
    private BrandCategoryService brandCategoryService;

    /**
     * Add a new Brand Category test
     *
     * @throws ApiException
     */
    @Test
    public void addBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ", "  Dairy  ");
        BrandCategoryData data = brandCategoryDto.addBrandCategory(brandCategoryForm);
        BrandCategoryPojo pojo = brandCategoryService.getCheckForBrandCategory("amul", "dairy");
        assertEquals(pojo.getId(), data.getId());
        assertEquals(pojo.getBrand(), data.getBrand());
        assertEquals(pojo.getCategory(), data.getCategory());
    }

    /**
     * Add a duplicate Brand Category test i.e. Brand Category already exists
     *
     * @throws ApiException
     */
    @Test
    public void addDuplicateBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ", "  Dairy  ");
        brandCategoryDto.addBrandCategory(brandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand amul and Category dairy already exists");
        BrandCategoryForm newBrandCategoryForm = TestUtils.getBrandCategoryForm("Amul", "Dairy");
        brandCategoryDto.addBrandCategory(newBrandCategoryForm);
    }

    /**
     * Add a null Brand Category test
     *
     * @throws ApiException
     */
    @Test
    public void addNullBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null, "  Dairy  ");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No Brand provided");
        BrandCategoryData data = brandCategoryDto.addBrandCategory(brandCategoryForm);
    }

    /**
     * Fetch all BrandCategoryData test
     *
     * @throws ApiException
     */
    @Test
    public void getAllBrandCategoryTest() throws ApiException {
        BrandCategoryPojo firstBrandCategoryPojo = TestUtils.getBrandCategoryPojo("amul", "dairy");
        brandCategoryService.addBrandCategory(firstBrandCategoryPojo);
        BrandCategoryPojo secondBrandCategoryPojo = TestUtils.getBrandCategoryPojo("sudha", "dairy");
        brandCategoryService.addBrandCategory(secondBrandCategoryPojo);
        List<BrandCategoryData> data = brandCategoryDto.getAllBrandCategory();
        List<BrandCategoryPojo> pojos = brandCategoryService.selectAll();
        assertEquals(pojos.size(), data.size());
    }

    /**
     * Update a Brand Category test
     *
     * @throws ApiException
     */
    @Test
    public void updateBrandCategoryTest() throws ApiException {
        BrandCategoryPojo firstBrandCategoryPojo = TestUtils.getBrandCategoryPojo("amul", "dairy");
        BrandCategoryPojo pojo = brandCategoryService.addBrandCategory(firstBrandCategoryPojo);
        BrandCategoryForm updatedBrandCategoryForm = TestUtils.getBrandCategoryForm(" Sudha  ", "  Dairy ");
        BrandCategoryData updatedData = brandCategoryDto.updateBrandCategory(pojo.getId(), updatedBrandCategoryForm);
        BrandCategoryPojo expectedPojo = brandCategoryService.getCheckForBrandCategory("sudha", "dairy");
        assertEquals(expectedPojo.getId(), updatedData.getId());
        assertEquals(expectedPojo.getBrand(), updatedData.getBrand());
        assertEquals(expectedPojo.getCategory(), updatedData.getCategory());
    }

    /**
     * Fetching valid BrandId test
     *
     * @throws ApiException
     */
    @Test
    public void searchIdTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo("amul", "dairy");
        BrandCategoryPojo pojo = brandCategoryService.addBrandCategory(brandCategoryPojo);
        BrandCategoryData data = brandCategoryDto.getBrandCategory(pojo.getId());
        assertEquals(pojo.getId(), data.getId());
        assertEquals(pojo.getBrand(), data.getBrand());
        assertEquals(pojo.getCategory(), data.getCategory());
    }

    /**
     * Fetching an invalid BrandId test
     *
     * @throws ApiException
     */
    @Test
    public void searchInvalidIdTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ", "  Dairy  ");
        BrandCategoryData data = brandCategoryDto.addBrandCategory(brandCategoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand Category doesn't exist.");
        BrandCategoryData invalidData = brandCategoryDto.getBrandCategory(5);
    }

    /**
     * Adding blank Brand Category test
     *
     * @throws ApiException
     */
    @Test
    public void addBlankBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("    ", "    ");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No Brand provided");
        brandCategoryDto.addBrandCategory(brandCategoryForm);
    }
}

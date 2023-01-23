package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryDto inventoryDto;

    /**
     * Setting up of BrandDb,ProductDb
     * @throws ApiException
     */
    @Before
    public void init() throws ApiException {
        BrandCategoryPojo pojo = TestUtils.getBrandCategoryPojo("amul","dairy");
        brandCategoryService.addBrandCategory(pojo);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstProductPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstProductPojo);
        ProductPojo secondProductPojo = TestUtils.getProductpojo("am112",
                "one litre pasteurized milk",100.00,brandCategoryPojo.getId());
        productService.insertProduct(secondProductPojo);
        InventoryPojo firstInventoryPojo = TestUtils.getInventoryPojo(firstProductPojo.getId(),0);
        inventoryService.insert(firstInventoryPojo);
        InventoryPojo secondInventoryPojo = TestUtils.getInventoryPojo(secondProductPojo.getId(),0);
        inventoryService.insert(secondInventoryPojo);
    }

    /**
     * Default quantity of newly added product test
     * @throws ApiException
     */
    @Test
    public void getDefaultInventoryTest() throws ApiException {
        InventoryData inventoryData = inventoryDto.getInventory("am111");
        assertEquals((Integer) 0,inventoryData.getQuantity());
    }

    /**
     * Fetch all InventoryData test
     * @throws ApiException
     */
    @Test
    public void getAllInventoryTest() throws ApiException {
        List<InventoryData> list = inventoryDto.getAll();
        List<InventoryPojo> pojos = inventoryService.getAll();
        assertEquals(pojos.size(),list.size());
    }

    /**
     * Updating inventory test
     * @throws ApiException
     */
    @Test
    public void updateInventoryTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("am111",50);
        InventoryData inventoryData = inventoryDto.updateInventory(inventoryForm);
        ProductPojo productPojo = productService.getByBarcode("am111");
        InventoryPojo pojo = inventoryService.getInventory(productPojo.getId());
        assertEquals( productPojo.getBarcode(),inventoryData.getBarcode());
        assertEquals( pojo.getQuantity(),inventoryData.getQuantity());
        assertEquals(productPojo.getProduct(),inventoryData.getPName());
    }

    /**
     * Fetching invalid barcode test
     * @throws ApiException
     */
    @Test
    public void invalidBarcodeTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("xy111",50);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode doesn't exist:xy111");
        inventoryDto.updateInventory(inventoryForm);
    }
}

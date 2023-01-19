package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
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
    private BrandCategoryDto brandCategoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;

    private ProductData productData;

    /**
     * Setting up of BrandDb,ProductDb
     * @throws ApiException
     */
    @Before
    public void init() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        productData = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" One litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        productData = productDto.addProduct(secondProductForm);
    }

    /**
     * Default quantity of newly added product test
     * @throws ApiException
     */
    @Test
    public void getDefaultInventoryTest() throws ApiException {
        InventoryData inventoryData = inventoryDto.get(productData.getBarcode());
        assertEquals((Integer) 0,inventoryData.getQuantity());
    }

    /**
     * Fetch all InventoryData test
     * @throws ApiException
     */
    @Test
    public void getAllInventoryTest() throws ApiException {
        List<InventoryData> list = inventoryDto.getAll();
        assertEquals(2,list.size());
    }

    /**
     * Updating inventory test
     * @throws ApiException
     */
    @Test
    public void updateInventoryTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("am111",50);
        inventoryDto.update(inventoryForm);
        InventoryData inventoryData = inventoryDto.get("am111");
        assertEquals((Integer) 50,inventoryData.getQuantity());
        assertEquals("half litre pasteurized milk",inventoryData.getPName());
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
        inventoryDto.update(inventoryForm);
    }
}

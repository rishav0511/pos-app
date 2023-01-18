package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
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

    private ProductPojo productPojo;

    @Before
    public void init() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        productPojo = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" One litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        productPojo = productDto.addProduct(secondProductForm);
    }

    @Test
    public void getDefaultInventoryTest() throws ApiException {
        InventoryData inventoryData = inventoryDto.get(productPojo.getBarcode());
        assertEquals((Integer) 0,inventoryData.getQuantity());
    }

    @Test
    public void getAllInventoryTest() throws ApiException {
        List<InventoryData> list = inventoryDto.getAll();
        assertEquals(2,list.size());
    }

    @Test
    public void updateInventoryTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("am111",50);
        inventoryDto.update(inventoryForm);
        InventoryData inventoryData = inventoryDto.get("am111");
        assertEquals((Integer) 50,inventoryData.getQuantity());
        assertEquals("half litre pasteurized milk",inventoryData.getPName());
    }

    @Test(expected = ApiException.class)
    public void invalidBarcodeTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("xy111",50);
        inventoryDto.update(inventoryForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode doesn't exist");
    }
}

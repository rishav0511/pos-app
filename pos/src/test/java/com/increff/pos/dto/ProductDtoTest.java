package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.ProductData;
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

public class ProductDtoTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Autowired
    private ProductDto productDto;

    @Before
    public void addBrands() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        BrandCategoryForm secondBrandCategoryForm = TestUtils.getBrandCategoryForm("   nike  ","  footwear  ");
        brandCategoryDto.addBrand(secondBrandCategoryForm);
    }

    @Test
    public void addProductTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductPojo productPojo = productDto.addProduct(productForm);
        ProductData productData = productDto.get(productPojo.getId());
        assertEquals("half litre pasteurized milk",productData.getProduct());
        assertEquals("am111",productData.getBarcode());
        assertEquals((Double)50.75,productData.getMrp());
        assertEquals("amul",productData.getBName());
        assertEquals("dairy",productData.getBCategory());
    }

    @Test
    public void getAllProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                55.75," Amul ", "daiRY");
        productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        productDto.addProduct(secondProductForm);
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(2,productDataList.size());
    }

    @Test
    public void getByBarcodeProductTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductPojo productPojo = productDto.addProduct(productForm);
        ProductData productData = productDto.getByBarcode(productPojo.getBarcode());
        assertEquals("half litre pasteurized milk",productData.getProduct());
        assertEquals("am111",productData.getBarcode());
        assertEquals((Double)50.75,productData.getMrp());
        assertEquals("amul",productData.getBName());
        assertEquals("dairy",productData.getBCategory());
    }

    @Test
    public void updateProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                55.75," Amul ", "daiRY");
        ProductPojo pojo = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        ProductPojo productPojo = productDto.update(pojo.getId(),secondProductForm);
        ProductData productData = productDto.get(productPojo.getId());
        assertEquals("one litre pasteurized milk",productData.getProduct());
        assertEquals("am112",productData.getBarcode());
        assertEquals((Double)100.0,productData.getMrp());
        assertEquals("amul",productData.getBName());
        assertEquals("dairy",productData.getBCategory());
    }

    @Test(expected = ApiException.class)
    public void invalidBarcodeAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm("HaLF litre PasteurizED MIlk",null,
                55.75," Amul ", "daiRY");
        ProductPojo productPojo = productDto.addProduct(firstProductForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode, Name and a positive mrp!");
    }

    @Test(expected = ApiException.class)
    public void invalidNameAndMrpAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(null,"AM111",
                null," Amul ", "daiRY");
        ProductPojo productPojo = productDto.addProduct(firstProductForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode, Name and a positive mrp!");
    }

    @Test(expected = ApiException.class)
    public void barcodeRepeatTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductPojo productPojo = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM111",
                100.0," Amul ", "daiRY");
        productDto.addProduct(secondProductForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with same barcode already exists.");
    }
}

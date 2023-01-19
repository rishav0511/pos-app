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
        ProductData productData = productDto.addProduct(productForm);
        assertEquals("half litre pasteurized milk",productData.getProduct());
        assertEquals("am111",productData.getBarcode());
        assertEquals((Double)50.75,productData.getMrp());
        assertEquals("amul",productData.getBName());
        assertEquals("dairy",productData.getBCategory());
    }

    @Test
    public void addProductWithNonExistingBrandCategoryTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75,"Puma", "footwear");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand-Category doesn't exist");
        ProductData productData = productDto.addProduct(productForm);
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
    public void getByBarcodeTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData data = productDto.addProduct(productForm);
        ProductData fetchedProductData = productDto.getByBarcode(data.getBarcode());
        assertEquals("half litre pasteurized milk",fetchedProductData.getProduct());
        assertEquals("am111",fetchedProductData.getBarcode());
        assertEquals((Double)50.75,fetchedProductData.getMrp());
        assertEquals("amul",fetchedProductData.getBName());
        assertEquals("dairy",fetchedProductData.getBCategory());
    }

    @Test
    public void getByIdTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData data = productDto.addProduct(productForm);
        ProductData fetchedProductData = productDto.get(data.getId());
        assertEquals("half litre pasteurized milk",fetchedProductData.getProduct());
        assertEquals("am111",fetchedProductData.getBarcode());
        assertEquals((Double)50.75,fetchedProductData.getMrp());
        assertEquals("amul",fetchedProductData.getBName());
        assertEquals("dairy",fetchedProductData.getBCategory());
    }

    @Test
    public void getByInvalidIdTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData productData = productDto.addProduct(productForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with given Id doesn't exist, id:20");
        ProductData fetchedProductData = productDto.get(20);
    }

    @Test
    public void getByInvalidBarcodeTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData data= productDto.addProduct(productForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode doesn't exist:xy111");
        ProductData productData = productDto.getByBarcode("xy111");
    }

    @Test
    public void updateProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                55.75," Amul ", "daiRY");
        ProductData data = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        ProductData updatedData = productDto.update(data.getId(),secondProductForm);
        ProductData productData = productDto.get(updatedData.getId());
        assertEquals("one litre pasteurized milk",productData.getProduct());
        assertEquals("am112",productData.getBarcode());
        assertEquals((Double)100.0,productData.getMrp());
        assertEquals("amul",productData.getBName());
        assertEquals("dairy",productData.getBCategory());
    }

    @Test
    public void invalidBarcodeAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm("HaLF litre PasteurizED MIlk",null,
                55.75," Amul ", "daiRY");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode, Name and a positive mrp!");
        ProductData productData = productDto.addProduct(firstProductForm);
    }

    @Test
    public void invalidNameAndMrpAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(null,"AM111",
                null," Amul ", "daiRY");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode, Name and a positive mrp!");
        ProductData productData = productDto.addProduct(firstProductForm);
    }

    @Test
    public void barcodeRepeatTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData productData = productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM111",
                100.0," Amul ", "daiRY");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with same barcode already exists.");
        productDto.addProduct(secondProductForm);
    }
}

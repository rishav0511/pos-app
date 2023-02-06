package com.increff.pos.dto;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.helper.TestUtils;
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
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDto productDto;

    /**
     * Setting up BrandDb
     * @throws ApiException
     */
    @Before
    public void addBrands() throws ApiException {
        BrandCategoryPojo firstPojo = TestUtils.getBrandCategoryPojo("amul","dairy");
        brandCategoryService.addBrandCategory(firstPojo);
        BrandCategoryPojo secondPojo = TestUtils.getBrandCategoryPojo("nike","footwear");
        brandCategoryService.addBrandCategory(secondPojo);
    }

    /**
     * Adding a product test
     * @throws ApiException
     */
    @Test
    public void addProductTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        ProductData productData = productDto.addProduct(productForm);
        ProductPojo productPojo = productService.getByBarcode("am111");
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(productPojo.getBrandId());
        assertEquals(productPojo.getId(),productData.getId());
        assertEquals(productPojo.getProduct(),productData.getProduct());
        assertEquals(productPojo.getBarcode(),productData.getBarcode());
        assertEquals(productPojo.getMrp(),productData.getMrp());
        assertEquals(brandCategoryPojo.getBrand(),productData.getBName());
        assertEquals(brandCategoryPojo.getCategory(),productData.getBCategory());
    }

    /**
     * Adding a product with non-existing Brand Category
     * @throws ApiException
     */
    @Test
    public void addProductWithNonExistingBrandCategoryTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75,"Puma", "footwear");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand puma and Category footwear doesn't exists.");
        ProductData productData = productDto.addProduct(productForm);
    }

    /**
     * Fetching all productData test
     * @throws ApiException
     */
    @Test
    public void getAllProductTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        ProductPojo secondPojo = TestUtils.getProductpojo("am112",
                "one litre pasteurized milk",100.00,brandCategoryPojo.getId());
        productService.insertProduct(secondPojo);
        List<ProductData> productDataList = productDto.getAllProducts();
        List<ProductPojo> productPojos = productService.getAllProducts();
        assertEquals(productPojos.size(),productDataList.size());
    }

    /**
     * Fetching product by barcode test
     * @throws ApiException
     */
    @Test
    public void getByBarcodeTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        ProductData fetchedProductData = productDto.getByBarcode("am111");
        ProductPojo pojo = productService.getByBarcode("am111");
        assertEquals(pojo.getId(),fetchedProductData.getId());
        assertEquals(pojo.getProduct(),fetchedProductData.getProduct());
        assertEquals(pojo.getBarcode(),fetchedProductData.getBarcode());
        assertEquals(pojo.getMrp(),fetchedProductData.getMrp());
        assertEquals(brandCategoryPojo.getBrand(),fetchedProductData.getBName());
        assertEquals(brandCategoryPojo.getCategory(),fetchedProductData.getBCategory());
    }

    /**
     * Fetching product by productId test
     * @throws ApiException
     */
    @Test
    public void getByIdTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        ProductPojo pojo = productService.getByBarcode("am111");
        ProductData fetchedProductData = productDto.getProduct(pojo.getId());
        assertEquals(pojo.getId(),fetchedProductData.getId());
        assertEquals(pojo.getProduct(),fetchedProductData.getProduct());
        assertEquals(pojo.getBarcode(),fetchedProductData.getBarcode());
        assertEquals(pojo.getMrp(),fetchedProductData.getMrp());
        assertEquals(brandCategoryPojo.getBrand(),fetchedProductData.getBName());
        assertEquals(brandCategoryPojo.getCategory(),fetchedProductData.getBCategory());
    }

    /**
     * Fetching invalid productId test
     * @throws ApiException
     */
    @Test
    public void getByInvalidIdTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with given Id doesn't exist, id:20");
        ProductData fetchedProductData = productDto.getProduct(20);
    }

    /**
     * Fetching invalid barcode test
     * @throws ApiException
     */
    @Test
    public void getByInvalidBarcodeTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode doesn't exist:xy111");
        ProductData productData = productDto.getByBarcode("xy111");
    }

    /**
     * Update product test
     * @throws ApiException
     */
    @Test
    public void updateProductTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstPojo);
        ProductForm secondProductForm = TestUtils.getProductForm(" ONe litre PasteurizED MIlk ","AM111",
                100.0," Amul ", "daiRY");
        ProductData updatedData = productDto.updateProduct(firstPojo.getId(),secondProductForm);
        ProductPojo pojo = productService.getProduct(firstPojo.getId());
        assertEquals(pojo.getId(),updatedData.getId());
        assertEquals(pojo.getProduct(),updatedData.getProduct());
        assertEquals(pojo.getBarcode(),updatedData.getBarcode());
        assertEquals(pojo.getMrp(),updatedData.getMrp());
        assertEquals(brandCategoryPojo.getBrand(),updatedData.getBName());
        assertEquals(brandCategoryPojo.getCategory(),updatedData.getBCategory());
    }

    /**
     * Add product with invalid barcode test
     * @throws ApiException
     */
    @Test
    public void invalidBarcodeAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm("HaLF litre PasteurizED MIlk",null,
                55.75," Amul ", "daiRY");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode!");
        ProductData productData = productDto.addProduct(firstProductForm);
    }

    /**
     * Add product with invalid name and mrp test
     * @throws ApiException
     */
    @Test
    public void invalidNameAndMrpAddProductTest() throws ApiException {
        ProductForm firstProductForm = TestUtils.getProductForm(null,"AM111",
                null," Amul ", "daiRY");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Product Name!");
        ProductData productData = productDto.addProduct(firstProductForm);
    }

    /**
     * Add product with repeated barcode test
     * @throws ApiException
     */
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

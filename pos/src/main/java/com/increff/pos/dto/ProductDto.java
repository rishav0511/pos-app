package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackFor = ApiException.class)
    public ProductData addProduct(ProductForm productForm) throws ApiException {
        ValidationUtils.validateForm(productForm);
        BrandCategoryForm brandCategoryForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(brandCategoryForm.getBrand(), brandCategoryForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convertFormtoPojo(productForm, brandCategoryPojo);
        productService.insertProduct(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.insert(inventoryPojo);
        return ConvertUtil.convertPojotoData(productPojo,brandCategoryPojo);
    }

    public ProductData getProduct(Integer id) throws ApiException {
        ProductPojo productPojo = productService.getProduct(id);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(productPojo.getBrandId());
        return ConvertUtil.convertPojotoData(productPojo, brandCategoryPojo);
    }

    // todo Ask shubham for this
    public List<ProductData> getAllProducts() throws ApiException {
        List<ProductPojo> allProducts = productService.getAllProducts();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo pojo:allProducts){
            ProductData productData = ConvertUtil.convertPojotoData(pojo, brandCategoryService.select(pojo.getBrandId()));
            productDataList.add(productData);
        }
        return productDataList;
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(productPojo.getBrandId());
        ProductData productData = ConvertUtil.convertPojotoData(productPojo, brandCategoryPojo);
        return productData;
    }

    public ProductData updateProduct(Integer id, ProductForm productForm) throws ApiException {
        ValidationUtils.validateForm(productForm);
        BrandCategoryForm brandCategoryForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(brandCategoryForm.getBrand(), brandCategoryForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convertFormtoPojo(productForm, brandCategoryPojo);
        productPojo.setId(id);
        ProductPojo pojo = productService.updateProduct(id,productPojo);
        return ConvertUtil.convertPojotoData(pojo,brandCategoryPojo);
    }
}

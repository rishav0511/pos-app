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
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo addProduct(ProductForm productForm) throws ApiException {
        ValidationUtils.validateForm(productForm);
        BrandCategoryForm brandCategoryForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        NormalizeUtil.normalizeForm(brandCategoryForm);
        NormalizeUtil.normalizeForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(brandCategoryForm.getBrand(), brandCategoryForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convertFormtoPojo(productForm, brandCategoryPojo);
        ProductPojo p = productService.insert(productPojo);

        //throwing an error
//        productService.getByBarcode("xxx");

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.insert(inventoryPojo);
        return p;
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(productPojo.getBrandId());
        return ConvertUtil.convertPojotoData(productPojo, brandCategoryPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> pList = productService.getAll();
        List<ProductData> reqList = new ArrayList<>();
        for(ProductPojo p:pList){
            ProductData d = ConvertUtil.convertPojotoData(p, brandCategoryService.select(p.getBrandId()));
            reqList.add(d);
        }
        return reqList;
    }

    // Todo use this while inventory management create
    // Todo ProductDetails will extend from ProductData with some varible to show availability
    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(productPojo.getBrandId());
        ProductData productData = ConvertUtil.convertPojotoData(productPojo, brandCategoryPojo);
        return productData;
    }

    public ProductPojo update(Integer id,ProductForm productForm) throws ApiException {
        ValidationUtils.validateForm(productForm);
        BrandCategoryForm brandCategoryForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        NormalizeUtil.normalizeForm(brandCategoryForm);
        NormalizeUtil.normalizeForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory(brandCategoryForm.getBrand(), brandCategoryForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convertFormtoPojo(productForm, brandCategoryPojo);
        productPojo.setId(id);
        return productService.update(id,productPojo);
    }
}

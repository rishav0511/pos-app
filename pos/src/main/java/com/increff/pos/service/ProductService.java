
package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo insertProduct(ProductPojo productPojo) throws ApiException {
        NormalizeUtil.normalizePojo(productPojo);
        ProductPojo existing = productDao.select(productPojo.getBarcode());
        if (existing == null) {
            return productDao.insert(productPojo);
        } else {
            throw new ApiException("Product with same barcode already exists.");
        }
    }

    public ProductPojo getProduct(Integer id) throws ApiException {
        ProductPojo pojo = productDao.select(ProductPojo.class, id);
        if(pojo==null) {
            throw new ApiException("Product with given Id doesn't exist, id:"+id);
        }
        return pojo;
    }

    public List<ProductPojo> getAllProducts() {
        return productDao.selectAll();
    }

    public ProductPojo getByBarcode(String barcode) throws ApiException {
        barcode = StringUtil.toLowerCase(barcode);
        ProductPojo productPojo = productDao.select(barcode);
        if (productPojo == null) {
            throw new ApiException("Barcode doesn't exist:"+barcode);
        } else {
            return productPojo;
        }
    }

    public List<ProductPojo> getProductByBrandCategory(int brandId) {
        return productDao.getProductByBrandCategory(brandId);
    }

    @Transactional
    public ProductPojo updateProduct(Integer id, ProductPojo productPojo) throws ApiException {
        NormalizeUtil.normalizePojo(productPojo);
        ProductPojo existing = productDao.select(productPojo.getBarcode());
        if (existing != null && existing.getId() != id) {
            throw new ApiException("Barcode already taken by another product.");
        }
        return productDao.update(productPojo);
    }
}
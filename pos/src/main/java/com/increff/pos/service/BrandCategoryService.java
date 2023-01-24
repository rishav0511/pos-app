package com.increff.pos.service;

import com.increff.pos.dao.BrandCategoryDao;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandCategoryService {
    @Autowired
    private BrandCategoryDao brandCategoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public BrandCategoryPojo addBrandCategory(BrandCategoryPojo brandCategoryPojo) throws ApiException {
        NormalizeUtil.normalizePojo(brandCategoryPojo);
        getCheckBrandCategoryExist(brandCategoryPojo.getBrand(), brandCategoryPojo.getCategory());
        return brandCategoryDao.insert(brandCategoryPojo);
    }

    public BrandCategoryPojo select(Integer id) throws ApiException {
        return brandExists(id);
    }

    public List<BrandCategoryPojo> selectAll() {
        return brandCategoryDao.selectAll();
    }

    public List<BrandCategoryPojo> selectByBrand(String brand) {
        brand = StringUtil.toLowerCase(brand);
        return brandCategoryDao.selectByBrand(brand);
    }

    public List<BrandCategoryPojo> selectByCategory(String category) {
        category = StringUtil.toLowerCase(category);
        return brandCategoryDao.selectByCategory(category);
    }

    // todo ask shubham for Api exception
    @Transactional
    public BrandCategoryPojo updateBrandCategory(Integer id, BrandCategoryPojo brandCategoryPojo) throws ApiException {
        NormalizeUtil.normalizePojo(brandCategoryPojo);
        getCheckBrandCategoryExist(brandCategoryPojo.getBrand(), brandCategoryPojo.getCategory());
        BrandCategoryPojo existing = brandCategoryDao.select(id);
        if (existing != null && existing.getId() == id) {
            existing.setBrand(brandCategoryPojo.getBrand());
            existing.setCategory(brandCategoryPojo.getCategory());
            return brandCategoryDao.update(existing);
        } else {
            throw new ApiException("Error encountered while updating.");
        }
    }

    public BrandCategoryPojo brandExists(Integer id) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(id);
        if (brandCategoryPojo == null) {
            throw new ApiException("Brand Category doesn't exist.");
        }
        return brandCategoryPojo;
    }

    public void getCheckBrandCategoryExist(String brand, String category) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        BrandCategoryPojo brandCategoryPojo = brandCategoryDao.selectByBrandCategory(brand, category);
        if (brandCategoryPojo != null) {
            throw new ApiException("Brand " +brand + " and Category "+ category +" already exists");
        }
    }

    public BrandCategoryPojo getCheckForBrandCategory(String brand, String category) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        BrandCategoryPojo pojo = brandCategoryDao.selectByBrandCategory(brand, category);
        if (pojo == null) {
            throw new ApiException("Brand " +brand + " and Category "+ category +" doesn't exists.");
        }
        return pojo;
    }
}

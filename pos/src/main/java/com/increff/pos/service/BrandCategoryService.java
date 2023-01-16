package com.increff.pos.service;

import com.increff.pos.dao.BrandCategoryDao;
import com.increff.pos.pojo.BrandCategoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandCategoryService {
    @Autowired
    private BrandCategoryDao brandCategoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public BrandCategoryPojo insert(BrandCategoryPojo brandCategoryPojo) throws ApiException {
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
        return brandCategoryDao.selectByBrand(brand);
    }

    public List<BrandCategoryPojo> selectByCategory(String category) {
        return brandCategoryDao.selectByCategory(category);
    }

    @Transactional
    public BrandCategoryPojo update(Integer id, BrandCategoryPojo brandCategoryPojo) throws ApiException {
        getCheckBrandCategoryExist(brandCategoryPojo.getBrand(), brandCategoryPojo.getCategory());
        BrandCategoryPojo existing = brandCategoryDao.select(id);
        existing.setBrand(brandCategoryPojo.getBrand());
        existing.setCategory(brandCategoryPojo.getCategory());
        return brandCategoryDao.update(existing);
    }

    public BrandCategoryPojo brandExists(Integer id) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(id);
        if (brandCategoryPojo == null) {
            throw new ApiException("Brand with given id doesn't exist, id:" + id);
        }
        return brandCategoryPojo;
    }

    public void getCheckBrandCategoryExist(String brand, String category) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryDao.selectByBrandCategory(brand, category);
        if (brandCategoryPojo != null) {
            throw new ApiException("Brand and Category already exists");
        }
    }

    public BrandCategoryPojo getCheckForBrandCategory(String brand, String category) throws ApiException {
        BrandCategoryPojo pojo = brandCategoryDao.selectByBrandCategory(brand, category);
        if (pojo == null) {
            throw new ApiException("Brand-Category doesn't exist");
        }
        return pojo;
    }
}

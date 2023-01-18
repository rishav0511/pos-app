package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.increff.pos.util.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandCategoryDto {
    @Autowired
    private BrandCategoryService brandCategoryService;

    public BrandCategoryData addBrand(BrandCategoryForm brandCategoryForm) throws ApiException {
        ValidationUtils.validateForm(brandCategoryForm);
        NormalizeUtil.normalizeForm(brandCategoryForm);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convertFormtoPojo(brandCategoryForm);
        brandCategoryService.insert(brandCategoryPojo);
        return ConvertUtil.convertPojotoData(brandCategoryPojo);
    }

    public BrandCategoryData getBrand(Integer id) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(id);
        return ConvertUtil.convertPojotoData(brandCategoryPojo);
    }

    public List<BrandCategoryData> getAllBrand(){
        List<BrandCategoryPojo> pojos = brandCategoryService.selectAll();
        List<BrandCategoryData> brandDataList = new ArrayList<BrandCategoryData>();
        for(BrandCategoryPojo pojo:pojos){
            brandDataList.add(ConvertUtil.convertPojotoData(pojo));
        }
        return brandDataList;
    }

    public BrandCategoryData update(Integer id, BrandCategoryForm brandCategoryForm) throws ApiException {
        ValidationUtils.validateForm(brandCategoryForm);
        NormalizeUtil.normalizeForm(brandCategoryForm);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convertFormtoPojo(brandCategoryForm);
        BrandCategoryPojo pojo = brandCategoryService.update(id, brandCategoryPojo);
        return ConvertUtil.convertPojotoData(pojo);
    }
}

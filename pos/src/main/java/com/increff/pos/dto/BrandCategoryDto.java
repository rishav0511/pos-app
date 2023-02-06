package com.increff.pos.dto;

import com.increff.pos.model.data.BrandCategoryData;
import com.increff.pos.model.form.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandCategoryDto {
    @Autowired
    private BrandCategoryService brandCategoryService;

    public BrandCategoryData addBrandCategory(BrandCategoryForm brandCategoryForm) throws ApiException {
        ValidationUtils.validateForm(brandCategoryForm);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convertFormtoPojo(brandCategoryForm);
        brandCategoryService.addBrandCategory(brandCategoryPojo);
        return ConvertUtil.convertPojotoData(brandCategoryPojo);
    }

    public BrandCategoryData getBrandCategory(Integer id) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.select(id);
        return ConvertUtil.convertPojotoData(brandCategoryPojo);
    }

    public List<BrandCategoryData> getAllBrandCategory() {
        List<BrandCategoryPojo> pojos = brandCategoryService.selectAll();
        return pojos.stream().map(ConvertUtil::convertPojotoData).collect(Collectors.toList());
    }

    public BrandCategoryData updateBrandCategory(Integer id, BrandCategoryForm brandCategoryForm) throws ApiException {
        ValidationUtils.validateForm(brandCategoryForm);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convertFormtoPojo(brandCategoryForm);
        BrandCategoryPojo pojo = brandCategoryService.updateBrandCategory(id, brandCategoryPojo);
        return ConvertUtil.convertPojotoData(pojo);
    }
}

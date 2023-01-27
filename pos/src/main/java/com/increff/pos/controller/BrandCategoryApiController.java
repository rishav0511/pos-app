package com.increff.pos.controller;

import com.increff.pos.dto.BrandCategoryDto;
import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BrandCategoryApiController {
    @Autowired
    private BrandCategoryDto brandCategoryDto;

    @ApiOperation(value = "Add a brand")
    @RequestMapping(value = "/api/brands", method = RequestMethod.POST)
    public BrandCategoryData addBrandCategory(@RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
        return brandCategoryDto.addBrandCategory(brandCategoryForm);
    }

    @ApiOperation(value = "Gets a brand by Id")
    @RequestMapping(value = "/api/brands/{id}", method = RequestMethod.GET)
    public BrandCategoryData getBrandCategory(@PathVariable int id) throws ApiException {
        return brandCategoryDto.getBrandCategory(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brands", method = RequestMethod.GET)
    public List<BrandCategoryData> getAllBrandCategories() {
        return brandCategoryDto.getAllBrandCategory();
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
    public BrandCategoryData updateBrandCategory(@PathVariable int id, @RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
        return brandCategoryDto.updateBrandCategory(id, brandCategoryForm);
    }

}

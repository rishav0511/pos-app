package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductApiController {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds a Product")
    @RequestMapping(value = "/api/product", method = RequestMethod.POST)
    public ProductPojo addProduct(@RequestBody ProductForm productForm) throws ApiException {
        return productDto.addProduct(productForm);
    }


    @ApiOperation(value = "Gets a Product")
    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException {
        return productDto.get(id);
    }

    @ApiOperation(value = "Gets a Product")
    @RequestMapping(value = "/api/product/b/{barcode}", method = RequestMethod.GET)
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets list of all Products")
    @RequestMapping(value = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(value = "/api/product/{id}", method = RequestMethod.PUT)
    public ProductPojo update(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
        return productDto.update(id, productForm);
    }
}


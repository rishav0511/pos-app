package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
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
    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public ProductData addProduct(@RequestBody ProductForm productForm) throws ApiException {
        return productDto.addProduct(productForm);
    }


    @ApiOperation(value = "Gets a Product")
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public ProductData getProduct(@PathVariable int id) throws ApiException {
        return productDto.getProduct(id);
    }

    @ApiOperation(value = "Gets a Product")
    @RequestMapping(value = "/api/products/b/{barcode}", method = RequestMethod.GET)
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets list of all Products")
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public List<ProductData> getAllProducts() throws ApiException {
        return productDto.getAllProducts();
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.PUT)
    public ProductData updateProduct(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
        return productDto.updateProduct(id, productForm);
    }
}


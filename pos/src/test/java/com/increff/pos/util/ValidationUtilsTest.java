package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtilsTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void validateFormNullBarcodeTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("milky bar",null,15.0,"amul","dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Barcode!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNullProductTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm(null,"am123",15.0,"amul","dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter Product Name!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNullMrpTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("milky bar","am123",null,"amul","dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter mrp!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNegativeMrpTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("milky bar","am123",-5.0,"amul","dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a positive mrp!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNullBNameTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("milky bar","am123",5.0,null,"dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Brand Name!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNullBCategoryTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("milky bar","am123",5.0,"amul",null);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please Enter a Brand Category!");
        ValidationUtils.validateForm(productForm);
    }

    @Test
    public void validateFormNullBrandTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null,"dairy");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No Brand provided");
        ValidationUtils.validateForm(brandCategoryForm);
    }

    @Test
    public void validateFormNullCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("amul",null);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No Category provided");
        ValidationUtils.validateForm(brandCategoryForm);
    }

    @Test
    public void validateFormNegativeQuantityTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("am123",-5);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please enter a positive quantity");
        ValidationUtils.validateForm(inventoryForm);
    }

    @Test
    public void validateFormNullOrderItemTest() throws ApiException {
        List<String> barcodes = new ArrayList<>();
        List<Integer>quantities = new ArrayList<>();
        List<Double>sellingPrices = new ArrayList<>();
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Order items cannot be empty");
        ValidationUtils.validateForm(orderItemFormList);
    }

    @Test
    public void validateFormNegativeQuantityOrderTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("xy111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(-5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity cannot be less than or equal to 0");
        ValidationUtils.validateForm(orderItemFormList);
    }

    @Test
    public void validateFormNegativeSellingPriceOrderTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("xy111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(-52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling Price cannot be less than 0");
        ValidationUtils.validateForm(orderItemFormList);
    }

    @Test
    public void validateFormUserTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("xyz","xyz");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Invalid email!");
        ValidationUtils.validateForm(userForm);
    }

    @Test
    public void validateFormUserPasswordTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("xyz@xyz.com","     ");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Password must not be blank!");
        ValidationUtils.validateForm(userForm);
    }

}

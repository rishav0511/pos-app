package com.increff.pos.util;

import com.increff.pos.model.form.*;
import com.increff.pos.service.ApiException;

import java.util.List;

public class ValidationUtils {
    private static final String EMAIL_PATTERN = "[a-z\\d]+@[a-z]+\\.[a-z]{2,3}";

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }
    public static boolean isValidBarcode(String barcode) {
        return barcode.trim().matches("\\w+");
    }

    public static void validateForm(ProductForm productForm) throws ApiException {
        if (productForm.getBarcode() == null) {
            throw new ApiException("Please Enter Barcode!");
        } else if (!isValidBarcode(productForm.getBarcode())) {
            throw new ApiException("Invalid Barcode!");
        } else if (productForm.getProduct() == null) {
            throw new ApiException("Please Enter Product Name!");
        } else if (productForm.getMrp() == null) {
            throw new ApiException("Please Enter mrp!");
        } else if (productForm.getMrp() <= 0) {
            throw new ApiException("Please Enter a positive mrp!");
        } else if (productForm.getBName() == null) {
            throw new ApiException("Please Enter a Brand Name!");
        } else if (productForm.getBCategory() == null) {
            throw new ApiException("Please Enter a Brand Category!");
        }
    }

    public static void validateForm(BrandCategoryForm brandCategoryForm) throws ApiException {
        if (StringUtil.isEmpty(brandCategoryForm.getBrand())) {
            throw new ApiException("No Brand provided");
        } else if (StringUtil.isEmpty(brandCategoryForm.getCategory())) {
            throw new ApiException("No Category provided");
        }
    }

    public static void validateForm(InventoryForm inventoryForm) throws ApiException {
        if(inventoryForm.getQuantity()==null) {
            throw new ApiException("Please Enter quantity!");
        }
        else if(inventoryForm.getBarcode()==null) {
            throw new ApiException("Please Enter barcode!");
        }
        else if (inventoryForm.getQuantity() < 0) {
            throw new ApiException("Please enter a positive quantity");
        }
    }

    public static void validateForm(List<OrderItemForm> orderItemForms) throws ApiException {
        if (orderItemForms == null || orderItemForms.isEmpty()) {
            throw new ApiException("Order items cannot be empty");
        }
        for (OrderItemForm orderItem : orderItemForms) {
            if(orderItem.getQuantity() == null) {
                throw new ApiException("Please Enter quantity!");
            }
            else if(orderItem.getSellingPrice() == null) {
                throw new ApiException("Please Enter selling price!");
            }
            else if (orderItem.getQuantity() <= 0) {
                throw new ApiException("Quantity cannot be less than or equal to 0");
            } else if (orderItem.getSellingPrice() < 0) {
                throw new ApiException("Selling Price cannot be less than 0");
            }
        }
    }

    public static void validateForm(UserForm userForm) throws ApiException {
        if (!isValidEmail(userForm.getEmail())) {
            throw new ApiException("Invalid email!");
        }

        if (StringUtil.isEmpty(userForm.getPassword())) {
            throw new ApiException("Password must not be blank!");
        }

        if (StringUtil.isEmpty(userForm.getConfirmPassword())) {
            throw new ApiException("Please confirm password!");
        }
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            throw new ApiException("Passwords do not match!");
        }
    }
}

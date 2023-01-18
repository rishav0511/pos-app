package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ValidationUtils {
    private static final String EMAIL_PATTERN = "[a-z\\d]+@[a-z]+\\.[a-z]{2,3}";

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static void validateForm(ProductForm productForm) throws ApiException {
        if (productForm.getBarcode() == null || productForm.getProduct() == null || productForm.getMrp()==null || productForm.getMrp() <= 0) {
            throw new ApiException("Please Enter Barcode, Name and a positive mrp!");
        }
    }

    public static void validateForm(BrandCategoryForm brandCategoryForm) throws ApiException {
        if(StringUtil.isEmpty(brandCategoryForm.getCategory()) || StringUtil.isEmpty(brandCategoryForm.getBrand()) ){
            throw new ApiException("No brand and category provided");
        }
    }

    public static void validateForm(InventoryForm inventoryForm) throws ApiException {
        if (inventoryForm.getQuantity() < 0) {
            throw new ApiException("Please enter a positive quantity");
        }
    }

    public static void validateForm(List<OrderItemForm> orderItemForms) throws ApiException {
        if (orderItemForms == null || orderItemForms.isEmpty()) {
            throw new ApiException("Order items cannot be empty");
        }
        for (OrderItemForm orderItem : orderItemForms) {
            if (orderItem.getQuantity() <= 0) {
                throw new ApiException("Quantity cannot be less than or equal to 0");
            } else if (orderItem.getSellingPrice() < 0) {
                throw new ApiException("Selling Price cannot be less than 0");
            }
        }
    }

    public static void validateForm(SalesReportForm salesReportForm) {
        if(salesReportForm.getEndDate()==null) {
            salesReportForm.setEndDate(new Date());
        }
        if(salesReportForm.getStartDate()==null) {
            salesReportForm.setStartDate(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        }
        salesReportForm.setStartDate(TimeUtil.getStartOfDay(salesReportForm.getStartDate()));
        salesReportForm.setEndDate(TimeUtil.getEndOfDay(salesReportForm.getEndDate()));
    }

    public static void validateForm (UserForm userForm) throws ApiException {
        if (!isValidEmail(userForm.getEmail())) {
            throw new ApiException("Invalid email!");
        }

        if (StringUtil.isEmpty(userForm.getPassword())) {
            throw new ApiException("Password must not be blank!");
        }
    }
}

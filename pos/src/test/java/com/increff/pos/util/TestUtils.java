package com.increff.pos.util;

import com.increff.pos.model.*;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static BrandCategoryForm getBrandCategoryForm (String brand,String category) {
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand(brand);
        brandCategoryForm.setCategory(category);
        return brandCategoryForm;
    }

    public static ProductForm getProductForm (String product,String barcode,Double mrp,String bName, String bCategory) {
        ProductForm productForm = new ProductForm();
        productForm.setProduct(product);
        productForm.setBarcode(barcode);
        productForm.setMrp(mrp);
        productForm.setBName(bName);
        productForm.setBCategory(bCategory);
        return productForm;
    }

    public static InventoryForm getInventoryForm (String barcode,Integer quantity) {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return  inventoryForm;
    }

    public static List<OrderItemForm> getOrderItemArray(List<String>barcodes,
                                                        List<Integer>quantities, List<Double>sellingPrices) {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for (int i=0; i<barcodes.size(); i++) {
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setBarcode(barcodes.get(i));
            orderItemForm.setQuantity(quantities.get(i));
            orderItemForm.setSellingPrice(sellingPrices.get(i));
            orderItemFormList.add(orderItemForm);
        }
        return orderItemFormList;
    }

    public static SalesReportForm getSalesReportForm(String brand,String category) {
        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setBrand(brand);
        salesReportForm.setCategory(category);
        return salesReportForm;
    }

    public static UserForm getUserForm(String email,String password) {
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        return userForm;
    }
}

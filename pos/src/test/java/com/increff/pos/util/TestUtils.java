package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;

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

    public static BrandCategoryPojo getBrandCategoryPojo (String brand, String category) {
        BrandCategoryPojo pojo = new BrandCategoryPojo();
        pojo.setBrand(brand);
        pojo.setCategory(category);
        return pojo;
    }

    public static ProductPojo getProductpojo (String barcode, String product,Double mrp,Integer brandId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setProduct(product);
        productPojo.setMrp(mrp);
        productPojo.setBrandId(brandId);
        return productPojo;
    }

    public static InventoryPojo getInventoryPojo (int productId,int quantity) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static List<OrderItemPojo> getOrderItemPojoList (int orderId,List<ProductPojo>products,
                                                            List<Integer>quantities, List<Double>sellingPrices) {
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for (int i=0; i<products.size(); i++) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setProductId(products.get(i).getId());
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setQuantity(quantities.get(i));
            orderItemPojo.setSellingPrice(sellingPrices.get(i));
            orderItemPojos.add(orderItemPojo);
        }
        return orderItemPojos;
    }
}

package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.data.*;
import com.increff.pos.model.form.BrandCategoryForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConvertUtilsTest {

    @Test
    public void convertFormToBrandCategoryPojoTest() {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" amul ", " dairy ");
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convertFormtoPojo(brandCategoryForm);
        assertEquals(" amul ", brandCategoryPojo.getBrand());
        assertEquals(" dairy ", brandCategoryPojo.getCategory());
        assertEquals(null, brandCategoryPojo.getId());
    }

    @Test
    public void convertPojoToBrandCategoryDataTest() {
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo("brand", "category");
        brandCategoryPojo.setId(1);
        BrandCategoryData brandCategoryData = ConvertUtil.convertPojotoData(brandCategoryPojo);
        assertEquals("brand", brandCategoryData.getBrand());
        assertEquals("category", brandCategoryData.getCategory());
        assertEquals((Integer) 1, brandCategoryData.getId());
    }

    @Test
    public void convertProductFormToBrandFormTest() {
        ProductForm productForm = TestUtils.getProductForm("milky bar", "da123", 15.0, "amul", "dairy");
        BrandCategoryForm brandCategoryForm = ConvertUtil.convertProductFormtoBrandForm(productForm);
        assertEquals("amul", brandCategoryForm.getBrand());
        assertEquals("dairy", brandCategoryForm.getCategory());
    }

    @Test
    public void convertFormToProductPojoTest() {
        ProductForm productForm = TestUtils.getProductForm("milky bar", "da123", 15.0, "amul", "dairy");
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo("brand", "category");
        brandCategoryPojo.setId(1);
        ProductPojo productPojo = ConvertUtil.convertFormtoPojo(productForm, brandCategoryPojo);
        assertEquals("milky bar", productPojo.getProduct());
        assertEquals((Double) 15.0, productPojo.getMrp());
        assertEquals("da123", productPojo.getBarcode());
        assertEquals(1, productPojo.getBrandId());
    }

    @Test
    public void convertPojoToProductData() {
        ProductPojo productPojo = TestUtils.getProductpojo("da123", "milky bar", 15.0, 1);
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo("brand", "category");
        brandCategoryPojo.setId(1);
        ProductData productData = ConvertUtil.convertPojotoData(productPojo, brandCategoryPojo);
        assertEquals("milky bar", productData.getProduct());
        assertEquals("da123", productData.getBarcode());
        assertEquals((Double) 15.0, productData.getMrp());
        assertEquals("brand", productData.getBName());
        assertEquals("category", productData.getBCategory());
    }

    @Test
    public void convertPojoToInventoryData() {
        InventoryPojo inventoryPojo = TestUtils.getInventoryPojo(1, 50);
        ProductPojo productPojo = TestUtils.getProductpojo("da123", "milky bar", 15.0, 1);
        InventoryData inventoryData = ConvertUtil.convertPojotoData(inventoryPojo, productPojo);
        assertEquals("milky bar", inventoryData.getPName());
        assertEquals("da123", inventoryData.getBarcode());
        assertEquals((Integer) 50, inventoryData.getQuantity());
    }

    @Test
    public void convertFormToOrderItemPojo() {
        List<String> barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer> quantities = new ArrayList<>();
        quantities.add(6);
        List<Double> sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes, quantities, sellingPrices);
        OrderItemPojo orderItemPojo = ConvertUtil.convertFormToPojo(orderItemFormList.get(0), 1, 1);
        assertEquals((Integer) 6, orderItemPojo.getQuantity());
        assertEquals((Double) 52.0, orderItemPojo.getSellingPrice());
        assertEquals((Integer) 1, orderItemPojo.getOrderId());
        assertEquals((Integer) 1, orderItemPojo.getProductId());
    }

    @Test
    public void convertPojoToOrderItemData() {
        ProductPojo productPojo = TestUtils.getProductpojo("da123", "milky bar", 15.0, 1);
        List<ProductPojo> products = new ArrayList<>();
        products.add(productPojo);
        List<Integer> quantities = new ArrayList<>();
        quantities.add(6);
        List<Double> sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemPojo> orderItemPojos = TestUtils.getOrderItemPojoList(1, products, quantities, sellingPrices);
        OrderItemData orderItemData = ConvertUtil.convertPojoToData(orderItemPojos.get(0), productPojo);
        assertEquals((Integer) 6, orderItemData.getQuantity());
        assertEquals((Double) 52.0, orderItemData.getSellingPrice());
        assertEquals("da123", orderItemData.getBarcode());
        assertEquals("milky bar", orderItemData.getProduct());
    }

    @Test
    public void convertPojoToOrderData() {
        OrderPojo pojo = new OrderPojo();
        pojo.setId(1);
        ProductPojo productPojo = TestUtils.getProductpojo("da123", "milky bar", 15.0, 1);
        List<ProductPojo> products = new ArrayList<>();
        products.add(productPojo);
        List<Integer> quantities = new ArrayList<>();
        quantities.add(6);
        List<Double> sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemPojo> orderItemPojos = TestUtils.getOrderItemPojoList(pojo.getId(), products, quantities, sellingPrices);
        OrderData orderData = ConvertUtil.convertPojoToData(pojo, orderItemPojos);
        assertEquals(orderData.getCreatedAt(), pojo.getCreatedAt());
        assertEquals(orderData.getOrderId(), pojo.getId());
        assertEquals((Double) 312.0, orderData.getBillAmount());
    }

    @Test
    public void convertFormToUserPojo() {
        UserForm userForm = TestUtils.getUserForm("xyz@xyz.com", "xyz","xyz");
        UserPojo pojo = ConvertUtil.convertFormToPojo(userForm);
        assertEquals("xyz@xyz.com", pojo.getEmail());
        assertEquals("xyz", pojo.getPassword());
    }

    @Test
    public void convertPojoToUserData() {
        UserPojo userPojo = TestUtils.getUserPojo("xyz@xyz.com","xyz","supervisor");
        userPojo.setId(1);
        UserData userData = ConvertUtil.convertPojoToData(userPojo);
        assertEquals("xyz@xyz.com", userData.getEmail());
        assertEquals("supervisor", userData.getRole());
        assertEquals(1, userData.getId());
    }

    @Test
    public void setDailySalesReportPojoTest() {
        DailySalesReportPojo dailySalesReportPojo = ConvertUtil.setDailySalesReportPojo(new Date(), 1, 1, 10.0);
        assertEquals((Integer) 1, dailySalesReportPojo.getInvoiced_items_count());
        assertEquals((Integer) 1, dailySalesReportPojo.getInvoiced_orders_count());
        assertEquals((Double) 10.0, dailySalesReportPojo.getTotal_revenue());
        assertEquals(dailySalesReportPojo.getDate().getClass(), Date.class);
    }

    @Test
    public void setDailySalesReportDataTest() {
        DailySalesReportPojo dailySalesReportPojo = TestUtils.getDailySalesReportPojo((new Date()), 1, 1, 10.0);
        DailySalesReportData dailySalesReportData = ConvertUtil.getDailySalesReportData(dailySalesReportPojo);
        assertEquals(dailySalesReportPojo.getDate().getClass(), Date.class);
        assertEquals((Integer) 1, dailySalesReportData.getItemCount());
        assertEquals((Integer) 1, dailySalesReportData.getOrderCount());
        assertEquals((Double) 10.0, dailySalesReportData.getTotalRevenue());
    }

}

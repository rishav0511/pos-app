package com.increff.pos.dto;

import com.google.protobuf.Api;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderDto orderDto;
    private OrderPojo orderPojo;

    @Before
    public void init() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   Amul  ","  Dairy  ");
        brandCategoryDto.addBrand(firstBrandCategoryForm);
        ProductForm firstProductForm = TestUtils.getProductForm(" haLF litre PasteurizED MIlk ","AM111",
                50.75," Amul ", "daiRY");
        productDto.addProduct(firstProductForm);
        ProductForm secondProductForm = TestUtils.getProductForm(" One litre PasteurizED MIlk ","AM112",
                100.0," Amul ", "daiRY");
        productDto.addProduct(secondProductForm);
        InventoryForm firstInventoryForm = TestUtils.getInventoryForm("am111",10);
        inventoryDto.update(firstInventoryForm);
        InventoryForm secondInventoryForm = TestUtils.getInventoryForm("am112",10);
        inventoryDto.update(secondInventoryForm);
    }

    @Test
    public void addOrderTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        assertThat(new Date().after(orderPojo.getCreatedAt()), is(true));
        assertNotNull(orderPojo.getId());
        assertNotNull(orderPojo.getPath());
    }

    @Test
    public void updateOrderTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        barcodes.add("am112");
        quantities.add(5);
        sellingPrices.add(100.0);
        orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        String path = orderDto.updateOrder(orderPojo.getId(),orderItemFormList);
        assertEquals(path,orderPojo.getPath());
    }

    @Test(expected = ApiException.class)
    public void quantityExceededTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(20);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity not available for product, barcode:am111");
    }

    @Test(expected = ApiException.class)
    public void nonExistingBarcodeTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("xy111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(20);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Product with barcode xy111 not found");
    }

    @Test
    public void getAllOrdersTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        barcodes.add("am112");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(5);
        quantities.add(5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        sellingPrices.add(100.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        List<OrderData> data = orderDto.getAllOrders();
        assertEquals(1,data.size());
    }

    @Test
    public void getOrderItemsTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        InvoiceData invoiceData = orderDto.getInvoiceDataByOrderId(orderPojo.getId());
        List<OrderItemData>expected = new ArrayList<>();
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setProduct("half litre pasteurized milk");
        orderItemData.setBarcode("am111");
        orderItemData.setSellingPrice(52.0);
        orderItemData.setQuantity(5);
        expected.add(orderItemData);
        assertTrue(expected.size() == invoiceData.getOrderItemDataList().size()
                && expected.containsAll(invoiceData.getOrderItemDataList())
                && invoiceData.getOrderItemDataList().containsAll(expected));
    }

    @Test
    public void getPathForOrderTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        String path = orderDto.getFilePath(orderPojo.getId());
        assertNotNull(path);
    }

    @Test
    public void getOrderDetailsTest() throws ApiException {
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(5);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderPojo = orderDto.addOrder(orderItemFormList);
        OrderData orderData = orderDto.getOrderDetails(orderPojo.getId());
        assertNotNull(orderData.getOrderId());
        assertThat(new Date().after(orderData.getCreatedAt()), is(true));
        assertEquals((Double)260.0,orderData.getBillAmount());
    }

    @After
    public void deleteGeneratedInvoice() {
        try {
            Files.deleteIfExists(Paths.get(orderPojo.getPath()));
        } catch (Exception e) {
            System.out.println("Error encountered: " + e.getMessage());
        }
    }
}

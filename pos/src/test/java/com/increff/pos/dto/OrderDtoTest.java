package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        String[] barcodes = {"am111"};
        Integer [] quantities = {5};
        Double [] sellingPrices = {100.0};
        List<OrderItemForm> orderItemFormList = TestUtils
                .getOrderItemArray(Arrays.asList(barcodes),Arrays.asList(quantities),Arrays.asList(sellingPrices));
        OrderPojo orderPojo = orderDto.addOrder(orderItemFormList);
        assertThat(new Date().after(orderPojo.getCreatedAt()), is(true));
    }

}

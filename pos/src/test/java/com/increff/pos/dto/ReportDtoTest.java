package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ReportDtoTest extends AbstractUnitTest {
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
    @Autowired
    private ReportDto reportDto;

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
        List<String>barcodes = new ArrayList<>();
        barcodes.add("am111");
        List<Integer>quantities = new ArrayList<>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderDto.addOrder(orderItemFormList);
    }

    @Test
    public void getInventoryReportTest() {
        List<InventoryReportData> list = reportDto.getInventoryReport();
        assertEquals(1,list.size());
    }

    @Test
    public void getBrandCategoryReportTest() {
        List<BrandCategoryData> list = reportDto.getBrandCategoryReport();
        assertEquals(1,list.size());
    }

    @Test
    public void getSalesReportEmptyBrandCategoryTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("","");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    @Test
    public void getSalesReportEmptyBrandTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("","dairy");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    @Test
    public void getSalesReportEmptyCategoryTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("amul","");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    @Test
    public void getSalesReportTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("amul","dairy");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    @Test
    public void getDailyReportTest() throws ApiException {
        reportDto.generateDailySalesReport();
        List<DailySalesReportData> list = reportDto.getDailySalesReport();
        assertEquals(1,list.size());
    }
}

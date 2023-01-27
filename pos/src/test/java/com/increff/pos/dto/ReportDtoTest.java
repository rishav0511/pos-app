package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.helper.TestUtils;
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
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReportDto reportDto;

    /**
     * Setting up BrandDb,ProductDb,InventoryDb,OrderDb
     * @throws ApiException
     */
    @Before
    public void init() throws ApiException {
        BrandCategoryPojo pojo = TestUtils.getBrandCategoryPojo("amul","dairy");
        brandCategoryService.addBrandCategory(pojo);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckForBrandCategory("amul","dairy");
        ProductPojo firstProductPojo = TestUtils.getProductpojo("am111",
                "half litre pasteurized milk",55.75,brandCategoryPojo.getId());
        productService.insertProduct(firstProductPojo);
        ProductPojo secondProductPojo = TestUtils.getProductpojo("am112",
                "one litre pasteurized milk",100.00,brandCategoryPojo.getId());
        productService.insertProduct(secondProductPojo);
        InventoryPojo firstInventoryPojo = TestUtils.getInventoryPojo(firstProductPojo.getId(),0);
        inventoryService.insert(firstInventoryPojo);
        InventoryPojo secondInventoryPojo = TestUtils.getInventoryPojo(secondProductPojo.getId(),0);
        inventoryService.insert(secondInventoryPojo);
        OrderPojo orderPojo = orderService.createNewOrder();
        List<ProductPojo>products = new ArrayList<>();
        products.add(firstProductPojo);
        List<Integer>quantities = new ArrayList<>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<>();
        sellingPrices.add(52.0);
        List<OrderItemPojo> orderItemPojos = TestUtils.getOrderItemPojoList(orderPojo.getId(),products,quantities,sellingPrices);
        orderItemService.insertMultiple(orderItemPojos);
    }

    /**
     * Fetching inventory report test
     */
    @Test
    public void getInventoryReportTest() throws ApiException {
        List<InventoryReportData> list = reportDto.getInventoryReport();
        assertEquals(1,list.size());
    }

    /**
     * Fetching BrandCategory report test
     */
    @Test
    public void getBrandCategoryReportTest() {
        List<BrandCategoryData> list = reportDto.getBrandCategoryReport();
        assertEquals(1,list.size());
    }

    /**
     * Fetching sales report with empty brand category test
     * @throws ApiException
     */
    @Test
    public void getSalesReportEmptyBrandCategoryTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("","");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    /**
     * Fetching sales report with empty brand test
     * @throws ApiException
     */
    @Test
    public void getSalesReportEmptyBrandTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("","dairy");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    /**
     * Fetching sales report with empty category test
     * @throws ApiException
     */
    @Test
    public void getSalesReportEmptyCategoryTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("amul","");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    /**
     * Fetching sales report test
     * @throws ApiException
     */
    @Test
    public void getSalesReportTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm("amul","dairy");
        List<SalesReportData> list = reportDto.getSalesReport(salesReportForm);
        assertEquals(1,list.size());
    }

    /**
     * Fetching daily report test
     * @throws ApiException
     */
    @Test
    public void getDailyReportTest() throws ApiException {
        reportDto.generateDailySalesReport();
        List<DailySalesReportData> list = reportDto.getDailySalesReport();
        assertEquals(1,list.size());
    }
}

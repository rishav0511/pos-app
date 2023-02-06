package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/reports")
public class ReportsApiController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Generates Inventory Report")
    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return reportDto.getInventoryReport();
    }

    @ApiOperation(value = "Get sales report of brand-category")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form) throws ApiException {
        return reportDto.getSalesReport(form);
    }

    @ApiOperation(value = "Get daily sales report")
    @RequestMapping(value = "/daily-sales", method = RequestMethod.GET)
    public List<DailySalesReportData> getDailySalesReport() {
        return reportDto.getDailySalesReport();
    }

    @ApiOperation(value = "Gets brand-category report")
    @RequestMapping(value = "/brand", method = RequestMethod.GET)
    public List<BrandCategoryData> brandCategoryReport() throws ApiException {
        return reportDto.getBrandCategoryReport();
    }

}
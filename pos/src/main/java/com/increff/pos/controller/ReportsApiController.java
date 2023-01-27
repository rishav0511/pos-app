package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportsApiController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Generates Inventory Report")
    @RequestMapping(path = "api/reports/inventory",method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException{
        return reportDto.getInventoryReport();
    }

    @ApiOperation(value = "Get sales report of brand-category")
    @RequestMapping(path = "api/reports/sales", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport( @RequestBody SalesReportForm form) throws ApiException {
        return reportDto.getSalesReport(form);
    }

    @ApiOperation(value = "Get daily sales report")
    @RequestMapping(value = "/api/reports/daily-sales", method = RequestMethod.GET)
    public List<DailySalesReportData> getDailySalesReport() {
        return reportDto.getDailySalesReport();
    }

    @ApiOperation(value = "Gets brand-category report")
    @RequestMapping(value = "/api/reports/brand", method = RequestMethod.GET)
    public List<BrandCategoryData> brandCategoryReport() throws ApiException {
        return reportDto.getBrandCategoryReport();
    }

}
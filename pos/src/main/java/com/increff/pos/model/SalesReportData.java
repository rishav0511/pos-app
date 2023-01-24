package com.increff.pos.model;

import com.increff.pos.pojo.BrandCategoryPojo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData {
    private Double revenue;
    private Integer quantity;
    private String category;
    private String brand;
}

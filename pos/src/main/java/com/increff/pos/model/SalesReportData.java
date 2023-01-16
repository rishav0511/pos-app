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
    private Integer brandCategoryId;

    public SalesReportData(BrandCategoryPojo brandCategory, int i, double d) {
        this.brand = brandCategory.getBrand();
        this.category = brandCategory.getCategory();
        this.brandCategoryId = brandCategory.getId();
        this.quantity = i;
        this.revenue = d;
    }
}

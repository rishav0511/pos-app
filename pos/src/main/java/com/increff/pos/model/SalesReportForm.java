package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SalesReportForm {
    private String brand;
    private String category;
    private Date startDate;
    private Date endDate;
}

package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillData {
    private Integer id;
    private List<InvoiceData> items;
}

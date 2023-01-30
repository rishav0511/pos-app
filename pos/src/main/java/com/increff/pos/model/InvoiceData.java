package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    OrderData orderData;
    List<OrderItemData> orderItemDataList;
}

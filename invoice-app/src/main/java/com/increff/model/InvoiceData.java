package com.increff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    @JsonProperty("orderData")
    OrderData orderData;
    @JsonProperty("orderItemDataList")
    List<OrderItemData> orderItemDataList;
}

package com.increff.pos.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OrderItemData extends OrderItemForm {
    private String product;
}

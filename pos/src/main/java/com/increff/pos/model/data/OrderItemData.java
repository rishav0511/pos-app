package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderItemForm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OrderItemData extends OrderItemForm {
    private String product;
}

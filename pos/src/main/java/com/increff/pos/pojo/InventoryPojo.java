package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class InventoryPojo {
    @Id
    private Integer productId;
    private Integer quantity;
}

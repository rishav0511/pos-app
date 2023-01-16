
package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private String product;
    private String barcode;
    private Double mrp;
    private String bName;
    private String bCategory;
}

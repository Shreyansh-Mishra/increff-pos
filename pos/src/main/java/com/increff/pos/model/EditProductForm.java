package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProductForm {
    private String name;
    private String brandName;
    private String category;
    private Double mrp;
}

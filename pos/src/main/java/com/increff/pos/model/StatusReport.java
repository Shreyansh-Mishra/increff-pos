package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusReport {
    String brand;
    String category;
    String message;
    String barcode;
    Double mrp;
    String name;
    Integer quantity;

}

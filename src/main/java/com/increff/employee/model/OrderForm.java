package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {
	private String barcode;
	private int quantity;
	private double mrp;
}

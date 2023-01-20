package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {
	private int id;
	private double mrp;
	private String name;
	private String barcode;
	private String brandName;
	private String category;
}

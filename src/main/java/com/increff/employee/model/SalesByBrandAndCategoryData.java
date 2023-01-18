package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesByBrandAndCategoryData {
	private double revenue;
	private int quantity;
	private String brand;
	private String category;
}

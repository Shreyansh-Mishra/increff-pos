package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesByBrandAndCategoryData {
	private Double revenue;
	private Integer quantity;
	private String brand;
	private String category;
}

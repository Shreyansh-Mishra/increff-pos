package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {
	private String barcode;
	private Integer quantity;
	private String name;
	private Integer id;

}

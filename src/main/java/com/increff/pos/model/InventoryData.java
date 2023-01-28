package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {
	private String barcode;
	private int quantity;
	private String name;
	private int id;

}

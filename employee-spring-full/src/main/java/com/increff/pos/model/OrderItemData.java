package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
	private int orderId;
	private String itemName;
	private int quantity;
	private double mrp;
	private String barcode;

	private double cost;

}

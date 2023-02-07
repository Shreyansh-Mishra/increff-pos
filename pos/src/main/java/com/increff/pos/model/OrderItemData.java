package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
	private Integer orderId;
	private String itemName;
	private Integer quantity;
	private Double mrp;
	private String barcode;

	private Double cost;

}

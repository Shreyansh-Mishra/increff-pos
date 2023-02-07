package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
public class OrderItemPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private Double mrp;
	@Transient
	private String barcode;
}

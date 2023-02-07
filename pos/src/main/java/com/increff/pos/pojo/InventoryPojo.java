package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
public class InventoryPojo {
	@Id
	private Integer id;
	private Integer quantity;
	@Transient
	private String barcode;
}

package com.increff.employee.pojo;

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
	private int id;
	private int quantity;
	@Transient
	private String barcode;
}

package com.increff.employee.pojo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
public class ProductPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String barcode;
	private String name;
	private double mrp;
	private int brand_category;
	@Transient
	private String brandName;
	@Transient
	private String category;

}

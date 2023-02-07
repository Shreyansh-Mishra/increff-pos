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
public class ProductPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String barcode;
	private String name;
	private Double mrp;
	private Integer brand_category;
	@Transient
	private String brandName;
	@Transient
	private String category;

}

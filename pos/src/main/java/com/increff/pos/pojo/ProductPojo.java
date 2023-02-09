package com.increff.pos.pojo;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="Products", uniqueConstraints = {@UniqueConstraint(name="barcode",columnNames = {"barcode"})})
public class ProductPojo{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NonNull
	@Column(nullable = false)
	private String barcode;
	@NonNull
	@Column(nullable = false)
	private String name;
	@NonNull
	@Column(nullable = false)
	private Double mrp;
	@NonNull
	@Column(nullable = false)
	private Integer brand_category;
	@Transient
	private String brandName;
	@Transient
	private String category;

}

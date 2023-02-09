package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name="Brands",uniqueConstraints = {@UniqueConstraint(name="brand_category",columnNames = {"brand", "category"})})
public class BrandPojo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(nullable = false)
	private String brand;
	@NotNull
	@Column(nullable = false)
	private String category;

}

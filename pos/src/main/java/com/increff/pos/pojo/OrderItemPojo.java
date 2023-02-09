package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "OrderItems")
public class OrderItemPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NonNull
	@Column(nullable = false)
	private Integer orderId;
	@NonNull
	@Column(nullable = false)
	private Integer productId;
	@NonNull
	@Column(nullable = false)
	private Integer quantity;
	@NonNull
	@Column(nullable = false)
	private Double mrp;
}

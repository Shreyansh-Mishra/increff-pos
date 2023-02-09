package com.increff.pos.pojo;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class OrderPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NonNull
	@Column(nullable = false)
	private Instant time;
	@PrePersist
	protected void onCreate() {
		//get instant in utc
		Instant it = Instant.now();
		time = it;
	}
	
}

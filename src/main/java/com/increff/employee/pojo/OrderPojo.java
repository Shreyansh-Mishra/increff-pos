package com.increff.employee.pojo;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Getter
@Setter
@Entity
public class OrderPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Instant time;
	@PrePersist
	protected void onCreate() {
		Instant it = Instant.now();
		time = it;
	}
	
}

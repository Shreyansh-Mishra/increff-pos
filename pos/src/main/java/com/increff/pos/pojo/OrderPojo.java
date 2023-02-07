package com.increff.pos.pojo;


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
	private Integer id;
	private Instant time;
	@PrePersist
	protected void onCreate() {
		//get instant in utc
		Instant it = Instant.now();
		System.out.println(it);
		time = it;
	}
	
}

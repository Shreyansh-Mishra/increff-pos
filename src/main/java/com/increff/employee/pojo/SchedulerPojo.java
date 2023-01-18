package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class SchedulerPojo {
	@Id
	private Instant date;
	private int invoiced_orders_count;
	private int invoiced_items_count;
	private double revenue;
}

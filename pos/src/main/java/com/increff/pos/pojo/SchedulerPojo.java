package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Scheduler")
public class SchedulerPojo {
	@Id
	private Instant date;
	private Integer invoiced_orders_count;
	private Integer invoiced_items_count;
	private Double revenue;
}

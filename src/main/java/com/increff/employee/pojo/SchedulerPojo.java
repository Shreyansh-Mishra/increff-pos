package com.increff.employee.pojo;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SchedulerPojo {
	@Id
	private Instant date;
	private int invoiced_orders_count;
	private int invoiced_items_count;
	private double revenue;
	public Instant getDate() {
		return date;
	}
	public void setDate(Instant date) {
		this.date = date;
	}
	public int getInvoiced_orders_count() {
		return invoiced_orders_count;
	}
	public void setInvoiced_orders_count(int invoiced_orders_count) {
		this.invoiced_orders_count = invoiced_orders_count;
	}
	public int getInvoiced_items_count() {
		return invoiced_items_count;
	}
	public void setInvoiced_items_count(int invoiced_items_count) {
		this.invoiced_items_count = invoiced_items_count;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	
	
}

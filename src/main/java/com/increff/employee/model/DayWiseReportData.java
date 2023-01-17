package com.increff.employee.model;


public class DayWiseReportData {
	private String date;
	private int invoiced_orders_count;
	private int invoiced_items_count;
	private double total_revenue;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getInvoiced_orders_count() {
		return invoiced_orders_count;
	}
	public void setInvoiced_orders_count(int invoiced_orders_count) {
		this.invoiced_orders_count = invoiced_orders_count;
	}
	public long getInvoiced_items_count() {
		return invoiced_items_count;
	}
	public void setInvoiced_items_count(int invoiced_items_count) {
		this.invoiced_items_count = invoiced_items_count;
	}
	public double getTotal_revenue() {
		return total_revenue;
	}
	public void setTotal_revenue(double total_revenue) {
		this.total_revenue = total_revenue;
	}
	
}

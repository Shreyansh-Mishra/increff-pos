package com.increff.pos.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayWiseReportData {
	private String date;
	private Integer invoiced_orders_count;
	private Integer invoiced_items_count;
	private Double total_revenue;
}

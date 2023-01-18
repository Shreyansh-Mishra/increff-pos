package com.increff.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="order")
public class OrderFOPObject {
	private String date;
	private int orderId;
	private List<OrderItemData> orderItems;
	
	@XmlElement(name="orderDate")
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@XmlElement(name="orderId")
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	@XmlElement(name="items")
	public List<OrderItemData> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemData> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}

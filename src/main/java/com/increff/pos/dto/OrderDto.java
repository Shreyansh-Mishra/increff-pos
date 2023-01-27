package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.pojo.*;
import com.increff.pos.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.InvoiceFOP;

import javax.transaction.Transactional;

@Component
public class OrderDto {

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private InvoiceFOP invoiceFop;

	@Transactional(rollbackOn = ApiException.class)
	public void createOrder(List<OrderForm> o) throws ApiException, Exception {
		List<OrderItemPojo> o2 = convert(o);
		for(OrderItemPojo item: o2){
			ProductPojo p = productService.selectByBarcode(item.getBarcode());
			InventoryPojo i = new InventoryPojo();
			InventoryPojo i2 = inventoryService.selectById(p.getId());
			i.setBarcode(p.getBarcode());
			int newQuantity = i2.getQuantity()-item.getQuantity();
			i.setQuantity(newQuantity);
			i.setId(p.getId());
			i.setBarcode(p.getBarcode());
			inventoryService.update(i);
			item.setProductId(p.getId());
		}
		OrderPojo order = new OrderPojo();
		orderService.addItems(o2,order);
		String path = invoiceFop.generatePdf(order.getId());
		InvoicePojo invoice = convert(order.getId(), path);
		orderService.insertInvoice(invoice);
	}

	
	public List<OrderData> getOrders(){
		List<OrderPojo> orders = orderService.selectOrders();
		List<OrderData> orderData = new ArrayList<OrderData>();
		for(OrderPojo order: orders) {
			orderData.add(convert(order));
		}
		return orderData;
	}
	
	public List<OrderItemData> getOrderItems(int id) throws ApiException{
		List<OrderItemPojo> items = orderService.selectItems(id);
		List<OrderItemData> orderItems = new ArrayList<OrderItemData>();
		for(OrderItemPojo i: items) {
			OrderItemData o = convert(i);
			orderItems.add(o);
		}
		return orderItems;
	}

	public InvoicePojo getInvoice(int id) throws ApiException{
		InvoicePojo invoice = orderService.selectInvoice(id);
		return invoice;
	}
	
	public static OrderData convert(OrderPojo order) {
		OrderData data = new OrderData();
		String time = order.getTime().toString();
		data.setId(order.getId());
		data.setTime(time);
		return data;
	}

	public static InvoicePojo convert(int id, String path){
		InvoicePojo invoice = new InvoicePojo();
		invoice.setId(id);
		invoice.setPath(path);
		return invoice;
	}
	
	public OrderItemData convert(OrderItemPojo o) throws ApiException {
		OrderItemData data = new OrderItemData();
		ProductPojo p = productService.selectById(o.getProductId());
		data.setBarcode(p.getBarcode());
		data.setItemName(p.getName());
		data.setOrderId(o.getOrderId());
		data.setQuantity(o.getQuantity());
		data.setSellingPrice(o.getSellingPrice());
		return data;
	}
	
	public static OrderItemPojo convert(OrderForm o) {
		OrderItemPojo o2 = new OrderItemPojo();
		o2.setQuantity(o.getQuantity());
		o2.setSellingPrice(o.getMrp());
		o2.setBarcode(o.getBarcode());
		return o2;
	}
	
	public static List<OrderItemPojo> convert(List<OrderForm> form){
		List<OrderItemPojo> list= new ArrayList<OrderItemPojo>();
		for(OrderForm i: form) {
			OrderItemPojo o = convert(i);
			list.add(o);
		}
		return list;
	}
}

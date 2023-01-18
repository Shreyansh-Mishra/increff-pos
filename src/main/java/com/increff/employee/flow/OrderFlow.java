package com.increff.employee.flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.increff.employee.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.InvoiceFOP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OrderFlow {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	InvoiceFOP invoiceFop;
	
	public void createOrder(List<OrderForm> o) throws ApiException, Exception {
		if(o.size()==0) {
			throw new ApiException("Please add atleast 1 item to place your order!");
		}
		List<OrderItemPojo> o2 = convert(o);
		OrderPojo order = new OrderPojo();
		SchedulerPojo scheduler = new SchedulerPojo();
		orderService.addItems(o2,order);
		orderService.addToScheduler(scheduler, order.getId());
		String path = invoiceFop.generatePdf(order.getId());
		InvoicePojo invoice = convert(order.getId(), path);
		orderService.addInvoice(invoice);
	}
	
	public List<OrderData> getOrders(){
		List<OrderPojo> orders = orderService.getAllOrders();
		List<OrderData> orderData = new ArrayList<OrderData>();
		for(OrderPojo order: orders) {
			orderData.add(convert(order));
		}
		return orderData;
	}
	
	public List<OrderItemData> getOrderItems(int id) throws ApiException{
		List<OrderItemPojo> items = orderService.getItems(id);
		List<OrderItemData> orderItems = new ArrayList<OrderItemData>();
		for(OrderItemPojo i: items) {
			OrderItemData o = convert(i);
			orderItems.add(o);
		}
		return orderItems;
	}

	public InvoicePojo getInvoice(int id) throws ApiException, Exception{
		InvoicePojo invoice = orderService.getInvoice(id);
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
		ProductPojo p = productService.getById(o.getProductId());
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

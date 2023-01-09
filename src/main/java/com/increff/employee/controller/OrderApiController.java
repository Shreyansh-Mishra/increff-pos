package com.increff.employee.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {
	@Autowired
	OrderService orderService;
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	@ApiOperation(value = "Create an order")
	@RequestMapping(path= "/api/order/add-order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderForm> o) throws ApiException {
		OrderPojo order = new OrderPojo();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		order.setTime(sdf.format(cal.getTime()));
		orderService.add(order);
		List<String> exceptions = new ArrayList<String>();
		for(OrderForm i: o) {
			OrderItemPojo o2 = convert(i);
			String s = orderService.addItems(o2, i.getBarcode(),order.getId());
			if(s.length()>0) {
				exceptions.add(s);
			}
		}
		if(exceptions.size()==o.size()) {
			orderService.deleteOrder(order.getId());
		}
		if(exceptions.size()>0) {
			String s = String.join("\n",exceptions);
			throw new ApiException(s);
		}
	}
	
	@ApiOperation(value = "Get all orders")
	@RequestMapping(path="/api/order/get-orders", method=RequestMethod.GET)
	public List<OrderPojo> getOrders() {
		return orderService.getAllOrders();
	}
	
	@ApiOperation(value = "Get order items")
	@RequestMapping(path="/api/order/{id}", method=RequestMethod.GET)
	public List<OrderItemPojo> getOrderItems(@PathVariable int id){
		return orderService.getItems(id);
	}
	
	public static OrderItemPojo convert(OrderForm o) {
		OrderItemPojo o2 = new OrderItemPojo();
		o2.setQuantity(o.getQuantity());
		o2.setSellingPrice(o.getMrp());
		return o2;
	}
}

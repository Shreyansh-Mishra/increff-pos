package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@ApiOperation(value = "Create an order")
	@RequestMapping(path= "/api/order/create-order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderForm> o) throws ApiException {
		OrderPojo order = new OrderPojo();
		Date date = new Date();
		order.setTime(date.getTime());
		List<String> exceptions = new ArrayList<String>();
		orderService.add(order);
		for(OrderForm i: o) {
			OrderItemPojo o2 = convert(i);
			String s = orderService.addItems(o2, i.getBarcode(),order.getId());
			if(s.length()>0) {
				exceptions.add(s);
			}
		}
		System.out.print(o.size());
		System.out.println(exceptions.size());
		if(exceptions.size()==o.size()) {
			orderService.deleteOrder(order.getId());
		}
		if(exceptions.size()>0) {
			String s = String.join("\n",exceptions);
			throw new ApiException(s);
		}
	}
	
	public static OrderItemPojo convert(OrderForm o) {
		OrderItemPojo o2 = new OrderItemPojo();
		o2.setQuantity(o.getQuantity());
		o2.setSellingPrice(o.getMrp());
		return o2;
	}
}

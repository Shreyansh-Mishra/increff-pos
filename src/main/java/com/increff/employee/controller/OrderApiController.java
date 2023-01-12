package com.increff.employee.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.flow.OrderFlow;
import com.increff.employee.model.OrderForm;
import com.increff.employee.model.OrderItemData;
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
	
	@Autowired
	OrderFlow orderFlow;
	
	@ApiOperation(value = "Create an order")
	@RequestMapping(path= "/api/order/add-order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderForm> o) throws ApiException {
		orderFlow.createOrder(o);
	}
	
	@ApiOperation(value = "Get all orders")
	@RequestMapping(path="/api/order/get-orders", method=RequestMethod.GET)
	public List<OrderPojo> getOrders() {
		return orderFlow.getOrders();
	}
	
	@ApiOperation(value = "Get order items")
	@RequestMapping(path="/api/order/{id}", method=RequestMethod.GET)
	public List<OrderItemData> getOrderItems(@PathVariable int id) throws ApiException{
		return orderFlow.getOrderItems(id);
	}	
}

package com.increff.employee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


import com.increff.employee.pojo.InvoicePojo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import com.increff.employee.flow.OrderFlow;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Api
@RestController
public class OrderApiController {
	@Autowired
	OrderFlow orderFlow;
	
	@ApiOperation(value = "Create an order")
	@RequestMapping(path= "/api/order/add-order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderForm> o) throws ApiException, Exception {
		orderFlow.createOrder(o);
	}
	
	@ApiOperation(value = "Get all orders")
	@RequestMapping(path="/api/order/get-orders", method=RequestMethod.GET)
	public List<OrderData> getOrders() {
		return orderFlow.getOrders();
	}
	
	@ApiOperation(value = "Get order items")
	@RequestMapping(path="/api/order/{id}", method=RequestMethod.GET)
	public List<OrderItemData> getOrderItems(@PathVariable int id) throws ApiException{
		return orderFlow.getOrderItems(id);
	}

	@ApiOperation(value = "Get Invoice")
	@RequestMapping(path="/api/order/invoice/{id}", method=RequestMethod.GET, produces = "application/pdf")
	public StreamingResponseBody getInvoice(@PathVariable int id, HttpServletResponse response) throws Exception{
		//get file with the name as id
		InvoicePojo invoice = orderFlow.getInvoice(id);
		File file = new File(invoice.getPath());

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName()+"");
		InputStream inputStream = new FileInputStream(new File(invoice.getPath()));

		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				System.out.println("Writing some bytes..");
				outputStream.write(data, 0, nRead);
			}
		};
	}
}

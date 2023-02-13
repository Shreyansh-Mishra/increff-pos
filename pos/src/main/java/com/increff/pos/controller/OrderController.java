package com.increff.pos.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


import com.increff.pos.pojo.InvoicePojo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Api
@RestController
@ControllerAdvice
@RequestMapping(path = "/api")
public class OrderController {
	@Autowired
    OrderDto orderDto;
	
	@ApiOperation(value = "Create an order")
	@RequestMapping(path= "/order", method=RequestMethod.POST)
	public OrderData createOrder(@RequestBody List<OrderForm> o) throws Exception {
		return orderDto.createOrder(o);
	}
	
	@ApiOperation(value = "Get all orders")
	@RequestMapping(path="/order", method=RequestMethod.GET)
	public List<OrderData> getOrders() {
		return orderDto.getOrders();
	}
	
	@ApiOperation(value = "Get order items")
	@RequestMapping(path="/order/{id}", method=RequestMethod.GET)
	public List<OrderItemData> getOrderItems(@PathVariable Integer id) throws ApiException{
		return orderDto.getOrderItems(id);
	}

	@ApiOperation(value = "Get Invoice")
	@RequestMapping(path="/order/invoice/{id}", method=RequestMethod.GET, produces = "application/pdf")
	public StreamingResponseBody getInvoice(@PathVariable Integer id, HttpServletResponse response) throws Exception{
		//get file with the name as id
		InvoicePojo invoice = orderDto.getInvoice(id);
		File file = new File(invoice.getPath());

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName() + "");
		InputStream inputStream = new FileInputStream(new File(invoice.getPath()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Integer nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			baos.write(data, 0, nRead);
		}

		response.setContentLength(baos.size());
		return outputStream -> baos.writeTo(outputStream);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
	}
}

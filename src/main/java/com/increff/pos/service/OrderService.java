package com.increff.pos.service;


import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private InvoiceDao invoiceDao;


	@Transactional(rollbackOn = ApiException.class)
	public void addOrder(OrderPojo orderPojo) throws ApiException {
		orderDao.insert(orderPojo);
	}

	@Transactional
	public List<OrderPojo> selectOrders(){
		return orderDao.selectAll();
	}

	@Transactional
	public OrderPojo selectOrderById(int id) {
		return orderDao.selectId(id);
	}
	
	@Transactional
	public List<OrderPojo> selectOrdersBetweenDates(Instant startDate, Instant endDate){
		System.out.println(endDate);
		return orderDao.selectBetweenDates(startDate, endDate);
	}

	@Transactional
	public void insertInvoice(InvoicePojo i){
		invoiceDao.insert(i);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InvoicePojo selectInvoice(int id) throws ApiException {
		InvoicePojo invoice = invoiceDao.selectId(id);
		if(invoice==null){
			throw new ApiException("Invoice with id "+id+" does not exist");
		}
		return invoice;
	}
	
}

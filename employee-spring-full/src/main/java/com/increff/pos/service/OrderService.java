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
@Transactional(rollbackOn = ApiException.class)
public class OrderService {
	@Autowired
	private OrderDao orderDao;

	public void addOrder(OrderPojo orderPojo) throws ApiException {
		orderPojo.setStatus("PENDING");
		orderDao.insert(orderPojo);
	}

	public List<OrderPojo> selectOrders(){
		return orderDao.selectAll();
	}

	public OrderPojo selectOrderById(Integer id) throws ApiException {
		OrderPojo order = orderDao.selectId(id);
		if(order == null) {
			throw new ApiException("Order with id " + id + " does not exist");
		}
		return order;
	}
	
	public List<OrderPojo> selectOrdersBetweenDates(Instant startDate, Instant endDate){
		return orderDao.selectBetweenDates(startDate, endDate);
	}

	public List<OrderPojo> selectByDate(Instant from, Instant to){
		return orderDao.selectByDate(from,to);
	}

	public void checkStatus(OrderPojo order) throws ApiException{
		if(order.getStatus().equals("INVOICED")) {
			throw new ApiException("Order with id " + order.getId() + " is already invoiced");
		}
	}
}

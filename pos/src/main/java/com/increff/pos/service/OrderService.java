package com.increff.pos.service;


import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {
	@Autowired
	private OrderDao orderDao;

	public OrderPojo addOrder(OrderPojo orderPojo) {
		return orderDao.insert(orderPojo);
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
	
	public List<OrderPojo> selectOrdersBetweenDates(Instant startDate, Instant endDate) throws ApiException {
		if(startDate.isAfter(endDate)) {
			throw new ApiException("Start date cannot be after end date");
		}
		//throw an exception if startDate and endDate have a difference of more than 6 months
		if(startDate.plusSeconds(15552000).isBefore(endDate)) {
			throw new ApiException("Start date and end date cannot be more than 6 months apart");
		}
		return orderDao.selectBetweenDates(startDate, endDate);
	}

	public List<OrderPojo> selectByDate(Instant from, Instant to){
		return orderDao.selectByDate(from,to);
	}
	
}

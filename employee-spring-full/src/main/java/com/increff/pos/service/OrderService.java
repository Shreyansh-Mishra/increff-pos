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
		orderDao.insert(orderPojo);
	}

	public List<OrderPojo> selectOrders(){
		return orderDao.selectAll();
	}

	public OrderPojo selectOrderById(int id) throws ApiException {
		OrderPojo order = orderDao.selectId(id);
		if(order == null) {
			throw new ApiException("Order with id " + id + " does not exist");
		}
		return order;
	}
	
	public List<OrderPojo> selectOrdersBetweenDates(Instant startDate, Instant endDate){
		return orderDao.selectBetweenDates(startDate, endDate);
	}

	public List<OrderPojo> selectByDate(Instant instant){
		return orderDao.selectByDate(instant);
	}
	
}

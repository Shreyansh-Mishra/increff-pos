package com.increff.pos.service;


import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private SchedulerDao schedulerDao;

	@Autowired
	private InvoiceDao invoiceDao;


	@Transactional(rollbackOn = ApiException.class)
	public void addItems(List<OrderItemPojo> orderItems,OrderPojo orderPojo) throws ApiException {
		orderDao.insert(orderPojo);
		if(orderItems.size()==0)
			throw new ApiException("Please add atleast 1 item to place your order!");
		for(OrderItemPojo o: orderItems) {
			if(o.getBarcode().isEmpty()||o.getQuantity()==0||o.getSellingPrice()==0) {
				throw new ApiException("All fields are mandatory, please check again!");
			}
			if(o.getQuantity()<0) {
				throw new ApiException("Quantity should be a positive value");
			}
			if(o.getSellingPrice()<0) {
				throw new ApiException("Selling Price needs to be positive");
			}
			o.setOrderId(orderPojo.getId());
			orderItemDao.insert(o);
		}
	}

	@Transactional
	public List<OrderPojo> selectOrders(){
		return orderDao.selectAll();
	}
	
	@Transactional
	public void insertScheduler(SchedulerPojo scheduler, int orderId) {
		OrderPojo o = orderDao.selectId(orderId);
		SchedulerPojo s = schedulerDao.checkExisting(o.getTime());
		List<OrderItemPojo> items = orderItemDao.selectItems(orderId);
		double revenue = 0;
		int itemcount = 0;
		for(OrderItemPojo item: items) {
			revenue+=(item.getSellingPrice()*item.getQuantity());
			itemcount += item.getQuantity();
		}
		if(s==null) {
			SchedulerPojo s2 = new SchedulerPojo();
			s2.setDate(o.getTime());
			s2.setInvoiced_items_count(items.size());
			s2.setInvoiced_orders_count(1);
			s2.setRevenue(revenue);
			schedulerDao.insert(s2);
		}
		else {
			s.setInvoiced_items_count(s.getInvoiced_items_count()+itemcount);
			s.setInvoiced_orders_count(s.getInvoiced_orders_count()+1);
			s.setRevenue(s.getRevenue()+revenue);
			schedulerDao.update(s);
		}
	}
	
	@Transactional
	public List<SchedulerPojo> selectSchedulerData() {
		return schedulerDao.select();
	}
	
	@Transactional
	public OrderPojo selectOrderById(int id) {
		return orderDao.selectId(id);
	}
	
	@Transactional
	public List<OrderItemPojo> selectItems(int id){
		return orderItemDao.selectItems(id);
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

	@Transactional
	public InvoicePojo selectInvoice(int id) throws ApiException {
		InvoicePojo invoice = invoiceDao.selectId(id);
		if(invoice==null){
			throw new ApiException("Invoice with id "+id+" does not exist");
		}
		return invoice;
	}
	
}

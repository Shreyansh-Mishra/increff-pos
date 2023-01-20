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
	OrderDao orderDao;
	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	ProductDao productDao;
	@Autowired
	OrderItemDao orderItemDao;
	@Autowired
	SchedulerDao schedulerDao;

	@Autowired
	InvoiceDao invoiceDao;
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	

	@Transactional(rollbackOn = ApiException.class)
	public void addItems(List<OrderItemPojo> orderItems,OrderPojo orderPojo) throws ApiException {
//		orderPojo.setTime(getTimestamp());
		orderDao.insert(orderPojo);
		for(OrderItemPojo o: orderItems) {
			if(o.getBarcode().isEmpty()||o.getQuantity()==0||o.getSellingPrice()==0) {
				throw new ApiException("All fields are mandatory, please check again!");
			}
			ProductPojo p = productDao.selectBarcode(o.getBarcode());
			if(o.getQuantity()<0) {
				throw new ApiException("Quantity should be a positive value");
			}
			if(o.getSellingPrice()<0) {
				throw new ApiException("Selling Price needs to be positive");
			}
			if(p==null) {
				throw new ApiException("Product with barcode "+o.getBarcode()+" is not present!");
			}
			o.setProductId(p.getId());
			InventoryPojo i = inventoryDao.selectId(p.getId());
			if(i==null) {
				throw new ApiException("The Product "+p.getName() +" isn't present in the Inventory");
			}
			int newQuantity = i.getQuantity()-o.getQuantity();
			if(newQuantity<0) {
				throw new ApiException("Not Enough quantity of "+p.getName()+" present in the Inventory");
			}
			
			i.setQuantity(newQuantity);
			o.setOrderId(orderPojo.getId());
			inventoryDao.update(i);
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

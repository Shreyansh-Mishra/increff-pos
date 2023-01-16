package com.increff.employee.service;


import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.dao.SchedulerDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.pojo.SchedulerPojo;

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
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	@Transactional
	public void add(OrderPojo o) {
		orderDao.insertOrder(o);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void addItems(List<OrderItemPojo> orderItems,OrderPojo orderPojo) throws ApiException {
//		orderPojo.setTime(getTimestamp());
		orderDao.insertOrder(orderPojo);
		for(OrderItemPojo o: orderItems) {
			if(o.getBarcode().isBlank()||o.getQuantity()==0||o.getSellingPrice()==0) {
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
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo checkIfExists(String barcode) throws ApiException {
		ProductPojo p = productDao.selectBarcode(barcode);
		if(p==null) {
			throw new ApiException("Product with barcode "+ barcode +" does not exists");
		}
		return p;
	}
	
	@Transactional
	public void deleteOrder(int id) {
		orderDao.delete(id);
	}
	
	@Transactional
	public List<OrderPojo> getAllOrders(){
		return orderDao.selectAll();
	}
	
	@Transactional
	public void addToScheduler(SchedulerPojo scheduler, int orderId) {
		OrderPojo o = orderDao.selectId(orderId);
		SchedulerPojo s = schedulerDao.checkExisting(o.getTime());
		List<OrderItemPojo> items = orderItemDao.selectItems(orderId);
		double revenue = 0;
		for(OrderItemPojo item: items) {
			revenue+=(item.getSellingPrice()*item.getQuantity());
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
			s.setInvoiced_items_count(s.getInvoiced_items_count()+items.size());
			s.setInvoiced_orders_count(s.getInvoiced_orders_count()+1);
			s.setRevenue(s.getRevenue()+revenue);
			schedulerDao.update(s);
		}
	}
	
	@Transactional
	public List<SchedulerPojo> getSchedulerData() {
		return schedulerDao.select();
	}
	
	
	public List<OrderItemPojo> getItems(int id){
		return orderItemDao.selectItems(id);
	}
	
	public List<Object[]> getOrdersByDate(Instant startDate, Instant endDate){
		return orderDao.selectByDate(startDate,endDate);
	}
	
	public List<Object[]> getReportByBrandAndCategory(Instant startDate, Instant endDate){
		return orderDao.selectBrandCategorySalesByDate(startDate, endDate);
	}
	
	public static String getTimestamp() {
		String ts = Instant.now().toString();
		ts=ts.replace('T', ' ');
		ts=ts.replace('Z',' ');
		ts = ts.substring(0, 16);
		return ts;
	}
	
}

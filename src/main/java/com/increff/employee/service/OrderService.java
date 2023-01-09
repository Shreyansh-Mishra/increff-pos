package com.increff.employee.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class OrderService {
	@Autowired
	OrderDao orderDao;
	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	ProductDao productDao;
	
	@Transactional
	public void add(OrderPojo o) {
		orderDao.insertOrder(o);
	}
	
	@Transactional
	public String addItems(OrderItemPojo o,String barcode,int id) throws ApiException {
		ProductPojo p = productDao.selectBarcode(barcode);
		if(p==null) {
			return "Product with barcode "+barcode+" is not present!";
		}
		else {
			o.setProductId(p.getId());
			o.setOrderId(id);
			InventoryPojo i = inventoryDao.selectId(p.getId());
			int newQuantity = i.getQuantity()-o.getQuantity();
			if(newQuantity<0) {
				return "Not Enough quantity of "+p.getName()+" present in the Inventory";
			}
			else {
				i.setQuantity(newQuantity);
				inventoryDao.update(i);
				orderDao.insert(o);
			}
		}
	return "";
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
}

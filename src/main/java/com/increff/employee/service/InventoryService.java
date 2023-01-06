package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class InventoryService {
	@Autowired
	private InventoryDao inventoryDao;
	@Autowired
	private ProductDao productDao;
	
	public void insert(InventoryPojo i) throws ApiException {
		ProductPojo p = productDao.selectId(i.getId());
		if(p==null) {
			throw new ApiException("The product does not exists");
		}
		inventoryDao.add(i);
	}
	
	public List<InventoryPojo> getWholeInventory(){
		return inventoryDao.selectAll();
	}
}

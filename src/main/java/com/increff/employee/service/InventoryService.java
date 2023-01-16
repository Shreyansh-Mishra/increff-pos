package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

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
	
	
	@Transactional(rollbackOn = ApiException.class)
	public void insert(InventoryPojo i) throws ApiException {
		ProductPojo p = checkExisting(i.getBarcode());
		InventoryPojo i2 = inventoryDao.selectId(p.getId());
		if(i2!=null) {
			i2.setQuantity(i.getQuantity()+i2.getQuantity());
		}
		else {
		i.setId(p.getId());
		inventoryDao.add(i);
		}
	}
	
	@Transactional
	public List<InventoryPojo> getWholeInventory(){
		return inventoryDao.selectAll();
	}
	
	@Transactional
	public InventoryPojo getById(int id) {
		return inventoryDao.selectId(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void update(InventoryPojo i) throws ApiException {
		ProductPojo p = checkExisting(i.getBarcode());
		InventoryPojo i2 = inventoryDao.selectId(p.getId());
		if(i2==null) {
			throw new ApiException("The Product isn't present in the Inventory");
		}
		i2.setQuantity(i.getQuantity());
		inventoryDao.update(i2);
	}
	
	@Transactional
	public ProductPojo checkExisting(String barcode) throws ApiException {
		ProductPojo p = productDao.selectBarcode(barcode);
		if(p==null) {
			throw new ApiException("The Product does not exists");
		}
		return p;
	}
	
	@Transactional
	public List<Object[]> getInventoryReport(){
		return inventoryDao.selectInventory();
	}
	
	
	
	
}

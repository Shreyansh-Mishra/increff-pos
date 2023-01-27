package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

@Service
public class InventoryService {
	@Autowired
	private InventoryDao inventoryDao;
	
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo inventoryItem) throws ApiException {
		if(isNegative(inventoryItem.getQuantity()))
			throw new ApiException("Quantity needs to be a positive value");

		InventoryPojo i2 = inventoryDao.selectId(inventoryItem.getId());
		if(i2!=null) {
			i2.setQuantity(inventoryItem.getQuantity()+i2.getQuantity());
			inventoryDao.update(i2);
		}
		else {
		inventoryDao.insert(inventoryItem);
		}
	}
	
	@Transactional
	public List<InventoryPojo> selectInventory(){
		return inventoryDao.selectAll();
	}
	
	@Transactional
	public InventoryPojo selectById(int id) throws ApiException {
		InventoryPojo i = inventoryDao.selectId(id);
		if(i==null)
			throw new ApiException("The product does not exists in the Inventory");
		return i;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void update(InventoryPojo inventoryItem) throws ApiException {
		if(isNegative(inventoryItem.getQuantity()))
			throw new ApiException("Enter a valid quantity for product with barcode "+inventoryItem.getBarcode());
		InventoryPojo i2 = inventoryDao.selectId(inventoryItem.getId());
		if(i2==null) {
			throw new ApiException("The Product isn't present in the Inventory");
		}
		i2.setQuantity(inventoryItem.getQuantity());
		inventoryDao.update(i2);
	}


	public static boolean isNegative(int a) {
		if(a<0)
			return true;
		return false;
	}
	
}

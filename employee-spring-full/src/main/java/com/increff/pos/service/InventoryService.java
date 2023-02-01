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
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {
	@Autowired
	private InventoryDao inventoryDao;
	

	public void add(InventoryPojo inventoryItem) throws ApiException {
		isNegative(inventoryItem.getQuantity(), inventoryItem.getBarcode());
		InventoryPojo i2 = inventoryDao.selectId(inventoryItem.getId());
		if(i2!=null) {
			i2.setQuantity(inventoryItem.getQuantity()+i2.getQuantity());
			inventoryDao.update(i2);
		}
		else {
		inventoryDao.insert(inventoryItem);
		}
	}
	
	public List<InventoryPojo> selectInventory(){
		return inventoryDao.selectAll();
	}
	
	public InventoryPojo selectById(int id) throws ApiException {
		InventoryPojo i = inventoryDao.selectId(id);
		if(i==null)
			throw new ApiException("The product does not exists in the Inventory");
		return i;
	}
	
	public void update(InventoryPojo inventoryItem) throws ApiException {
		isNegative(inventoryItem.getQuantity(), inventoryItem.getBarcode());
		InventoryPojo i2 = this.selectById(inventoryItem.getId());
		i2.setQuantity(inventoryItem.getQuantity());
		inventoryDao.update(i2);
	}
	public static void isNegative(int a, String barcode) throws ApiException {
		if(a<=0)
			throw new ApiException("Enter a valid quantity for product with barcode "+barcode);
	}

}

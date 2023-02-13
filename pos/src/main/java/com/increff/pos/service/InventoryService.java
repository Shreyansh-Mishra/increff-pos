package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {
	@Autowired
	private InventoryDao inventoryDao;
	

	public InventoryPojo add(InventoryPojo inventoryItem, String barcode) throws ApiException {
		isNegative(inventoryItem.getQuantity(), barcode);
		InventoryPojo inventory = inventoryDao.selectId(inventoryItem.getProductId());
		if(inventory!=null) {
			inventory.setQuantity(inventoryItem.getQuantity()+inventory.getQuantity());
			inventoryDao.update(inventory);
			return inventory;
		}
		else {
			return inventoryDao.insert(inventoryItem);
		}
	}

	public InventoryPojo selectByInvId(Integer id) throws ApiException {
		InventoryPojo inventory = inventoryDao.selectByInvId(id);
		if(inventory==null)
			throw new ApiException("The product does not exists in the Inventory");
		return inventory;
	}
	
	public List<InventoryPojo> selectInventory(){
		return inventoryDao.selectAll();
	}
	
	public InventoryPojo selectById(Integer id) throws ApiException {
		InventoryPojo inventory = inventoryDao.selectId(id);
		if(inventory==null)
			throw new ApiException("The product does not exists in the Inventory");
		return inventory;
	}
	
	public void update(InventoryPojo inventoryItem, String barcode) throws ApiException {
		isNegative(inventoryItem.getQuantity(), barcode);
		InventoryPojo inventory = this.selectByInvId(inventoryItem.getId());
		inventory.setQuantity(inventoryItem.getQuantity());
		inventoryDao.update(inventory);
	}
	public static void isNegative(Integer a, String barcode) throws ApiException {
		System.out.println(a);
		if(a<0){
			System.out.println("Enter a valid quantity for product with barcode "+barcode);
			throw new ApiException("Enter a valid quantity for product with barcode "+barcode);
		}
	}

}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

import javax.transaction.Transactional;

@Component
public class InventoryDto {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryData addToInventory(InventoryForm form) throws ApiException {
		//convert form to pojo
		InventoryPojo inventoryPojo = ObjectUtil.objectMapper(form, InventoryPojo.class);
		//get the product of the relevant barcode
		ProductPojo product = productService.selectByBarcode(inventoryPojo.getBarcode().toLowerCase());
		//set id of the product to the inventory pojo
		inventoryPojo.setId(product.getId());
		//add the inventory pojo to the database
		return convert(ObjectUtil.objectMapper(inventoryService.add(inventoryPojo), InventoryData.class),inventoryPojo);
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		//get all the inventory pojos from the database
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<InventoryData> inventoryData = new ArrayList<>();
		//convert each pojo to data and add to the list
		for(InventoryPojo item: inventory) {
			inventoryData.add(convert(ObjectUtil.objectMapper(item,InventoryData.class),item));
		}
		return inventoryData;
	}
	
	public InventoryData getById(Integer id) throws ApiException {
		//get the inventory pojo from the database and convert it to data
		return ObjectUtil.objectMapper(inventoryService.selectById(id), InventoryData.class);
	}
	
	public void editInventory(Integer id, InventoryForm form) throws ApiException {
		//convert form to pojo
		InventoryPojo inventory = ObjectUtil.objectMapper(form,InventoryPojo.class);
		inventory.setId(id);
		inventoryService.update(inventory);
	}

	//function to convert InventoryPojo to InventoryData by querying the product table
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}

	//function to get all the barcodes of the products in the inventory
	public List<String> getBarcodes() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<String> barcodes = new ArrayList<>();
		for(InventoryPojo item: inventory){
			barcodes.add(productService.selectById(item.getId()).getBarcode());
		}
		return barcodes;
	}

}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

@Component
public class InventoryFlow {
	@Autowired
	ProductService productService;
	
	@Autowired
	InventoryService inventoryService;
	
	
	public void addToInventory(InventoryForm form) throws ApiException {
		if(isNegative(form.getQuantity()))
			throw new ApiException("Quantity needs to be a positive value");
		InventoryPojo inventoryPojo = convert(form);
		inventoryService.add(inventoryPojo);
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		List<InventoryPojo> i = inventoryService.selectInventory();
		List<InventoryData> i2 = new ArrayList<InventoryData>();
		for(InventoryPojo j: i) {
			i2.add(convert(convert(j),j));
		}
		return i2;
	}
	
	public InventoryData getById(int id) {
		return convert(inventoryService.selectById(id));
	}
	
	public void editInventory(int id, InventoryForm i) throws ApiException {
		if(isNegative(i.getQuantity()))
			throw new ApiException("Quantity needs to be a positive value");
		InventoryPojo i2 = convert(i);
		inventoryService.update(i2);
	}
	
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}
	
	private static InventoryPojo convert(InventoryForm i) {
		InventoryPojo i2 = new InventoryPojo();
		i2.setQuantity(i.getQuantity());
		i2.setBarcode(i.getBarcode());
		return i2;
	}
	
	private static InventoryData convert(InventoryPojo i) {
		InventoryData i2 = new InventoryData();
		i2.setQuantity(i.getQuantity());
		i2.setId(i.getId());
		return i2;
	}
	
	public static boolean isNegative(int a) {
		if(a<0)
			return true;
		return false;
	}
}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.DtoUtil;
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
	public void addToInventory(InventoryForm form) throws ApiException {
		InventoryPojo inventoryPojo = DtoUtil.objectMapper(form, InventoryPojo.class);
		ProductPojo product = productService.selectByBarcode(inventoryPojo.getBarcode().toLowerCase());
		inventoryPojo.setId(product.getId());
		inventoryService.add(inventoryPojo);
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<InventoryData> inventoryData = new ArrayList<>();
		for(InventoryPojo item: inventory) {
			inventoryData.add(convert(DtoUtil.objectMapper(item,InventoryData.class),item));
		}
		return inventoryData;
	}
	
	public InventoryData getById(int id) throws ApiException {
		return DtoUtil.objectMapper(inventoryService.selectById(id), InventoryData.class);
	}
	
	public void editInventory(int id, InventoryForm form) throws ApiException {
		InventoryPojo inventory = DtoUtil.objectMapper(form,InventoryPojo.class);
		inventory.setId(id);
		inventoryService.update(inventory);
	}
	
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}

}

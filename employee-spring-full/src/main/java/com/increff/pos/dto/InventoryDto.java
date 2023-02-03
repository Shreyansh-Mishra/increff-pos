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
	ProductService productService;
	
	@Autowired
	InventoryService inventoryService;
	
	@Transactional(rollbackOn = ApiException.class)
	public void addToInventory(InventoryForm form) throws ApiException {
		InventoryPojo inventoryPojo = DtoUtil.objectMapper(form, InventoryPojo.class);
		ProductPojo product = productService.selectByBarcode(inventoryPojo.getBarcode().toLowerCase());
		inventoryPojo.setId(product.getId());
		inventoryService.add(inventoryPojo);
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		List<InventoryPojo> i = inventoryService.selectInventory();
		List<InventoryData> i2 = new ArrayList<>();
		for(InventoryPojo j: i) {
			i2.add(convert(DtoUtil.objectMapper(j,InventoryData.class),j));
		}
		return i2;
	}
	
	public InventoryData getById(int id) throws ApiException {
		return DtoUtil.objectMapper(inventoryService.selectById(id), InventoryData.class);
	}
	
	public void editInventory(int id, InventoryForm i) throws ApiException {

		InventoryPojo i2 = DtoUtil.objectMapper(i,InventoryPojo.class);
		i2.setId(id);
		inventoryService.update(i2);
	}
	
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}

}

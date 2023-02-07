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
		InventoryPojo inventoryPojo = ObjectUtil.objectMapper(form, InventoryPojo.class);
		ProductPojo product = productService.selectByBarcode(inventoryPojo.getBarcode().toLowerCase());
		inventoryPojo.setId(product.getId());
		return convert(ObjectUtil.objectMapper(inventoryService.add(inventoryPojo), InventoryData.class),inventoryPojo);
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<InventoryData> inventoryData = new ArrayList<>();
		for(InventoryPojo item: inventory) {
			inventoryData.add(convert(ObjectUtil.objectMapper(item,InventoryData.class),item));
		}
		return inventoryData;
	}
	
	public InventoryData getById(Integer id) throws ApiException {
		return ObjectUtil.objectMapper(inventoryService.selectById(id), InventoryData.class);
	}
	
	public void editInventory(Integer id, InventoryForm form) throws ApiException {
		InventoryPojo inventory = ObjectUtil.objectMapper(form,InventoryPojo.class);
		inventory.setId(id);
		inventoryService.update(inventory);
	}
	
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}

	public List<String> getBarcodes() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<String> barcodes = new ArrayList<>();
		for(InventoryPojo item: inventory){
			barcodes.add(productService.selectById(item.getId()).getBarcode());
		}
		return barcodes;
	}

}

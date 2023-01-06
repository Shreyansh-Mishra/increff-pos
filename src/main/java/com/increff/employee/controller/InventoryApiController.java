package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {
	@Autowired
	private InventoryService inventoryService;
	
	@ApiOperation(value="adds product to inventory")
	@RequestMapping(path="/api/inventory/add-product",method=RequestMethod.POST)
	public void addProductToInventory(@RequestBody InventoryForm i) throws ApiException {
		InventoryPojo i2 = convert(i);
		inventoryService.insert(i2);
	}
	
	@ApiOperation(value="view inventory")
	@RequestMapping(path="/api/inventory/view-inventory",method=RequestMethod.GET)
	public List<InventoryData> getWholeInventoryView() {
		List<InventoryPojo> i = inventoryService.getWholeInventory();
		List<InventoryData> i2 = new ArrayList<InventoryData>();
		for(InventoryPojo j: i) {
			i2.add(convert(j));
		}
		return i2;
	}
	
	private static InventoryPojo convert(InventoryForm i) {
		InventoryPojo i2 = new InventoryPojo();
		i2.setId(i.getId());
		i2.setQuantity(i.getQuantity());
		return i2;
	}
	
	private static InventoryData convert(InventoryPojo i) {
		InventoryData i2 = new InventoryData();
		i2.setId(i.getId());
		i2.setQuantity(i.getQuantity());
		return i2;
	}
	
}

package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {
	
	@Autowired
	private InventoryFlow inventoryFlow;
	
	@ApiOperation(value="adds product to inventory")
	@RequestMapping(path="/api/inventory/add-product",method=RequestMethod.POST)
	public void addProductToInventory(@RequestBody InventoryForm i) throws ApiException {
		inventoryFlow.addToInventory(i);
	}
	
	@ApiOperation(value="view inventory")
	@RequestMapping(path="/api/inventory/get-inventory",method=RequestMethod.GET)
	public List<InventoryData> getWholeInventory() throws ApiException {
		return inventoryFlow.getInventory();
	}
	
	@ApiOperation(value="Get by id")
	@RequestMapping(path="/api/inventory/{id}", method=RequestMethod.GET)
	public InventoryData getItemById(@PathVariable int id) {
		return inventoryFlow.getById(id);
	}
	
	@ApiOperation(value="edit Inventory")
	@RequestMapping(path="/api/inventory/{id}", method=RequestMethod.PUT)
	public void updateInventory(@PathVariable int id,@RequestBody InventoryForm i) throws ApiException {
		inventoryFlow.editInventory(id,i);
	}
	
}

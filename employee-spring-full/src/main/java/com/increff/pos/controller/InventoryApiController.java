package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class InventoryApiController {
	
	@Autowired
	private InventoryDto inventoryDto;
	
	@ApiOperation(value="adds product to inventory")
	@RequestMapping(path="/inventory",method=RequestMethod.POST)
	public InventoryData addProductToInventory(@RequestBody InventoryForm i) throws ApiException {
		return inventoryDto.addToInventory(i);
	}
	
	@ApiOperation(value="view inventory")
	@RequestMapping(path="/inventory",method=RequestMethod.GET)
	public List<InventoryData> getWholeInventory() throws ApiException {
		return inventoryDto.getInventory();
	}
	
	@ApiOperation(value="Get by id")
	@RequestMapping(path="/inventory/{id}", method=RequestMethod.GET)
	public InventoryData getItemById(@PathVariable Integer id) throws ApiException {
		return inventoryDto.getById(id);
	}
	
	@ApiOperation(value="edit Inventory")
	@RequestMapping(path="/inventory/{id}", method=RequestMethod.PUT)
	public void updateInventory(@PathVariable Integer id,@RequestBody InventoryForm i) throws ApiException {
		inventoryDto.editInventory(id,i);
	}
	
}

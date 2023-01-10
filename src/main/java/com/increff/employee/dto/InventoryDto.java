package com.increff.employee.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.InventoryData;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

@Component
public class InventoryDto {
	@Autowired
	ProductService productService;
	
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.getById(i2.getId());
		i.setBarcode(p.getBarcode());
		return i;
	}
}

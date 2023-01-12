package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.flow.BrandFlow;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
	@Autowired
	private BrandFlow brandFlow;
	
	
	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/api/brand/add-brand", method = RequestMethod.POST)
	public void addBrand(@RequestBody BrandForm form) throws ApiException {
		brandFlow.createBrand(form);
	}
	
	@ApiOperation(value= "Get all brands")
	@RequestMapping(path= "/api/brand/get-brands", method = RequestMethod.GET)
	public List<BrandData> getBrands(){
		return brandFlow.getAllBrands();
	}
	
	@ApiOperation(value= "Get brand by name and category")
	@RequestMapping(path= "/api/brand/{name}/{category}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable String name,@PathVariable String category) throws ApiException{
		return brandFlow.getBrandByNameAndCategory(name, category);
	}
	
	@ApiOperation(value= "Get brand by id")
	@RequestMapping(path= "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable int id) throws ApiException{
		return brandFlow.getBrandById(id);
	}
	
	@ApiOperation(value= "Get brand by name")
	@RequestMapping(path= "/api/brand/name/{name}", method = RequestMethod.GET)
	public List<BrandData> getBrands(@PathVariable String name) throws ApiException{
		return brandFlow.getBrandsByName(name);
	}
	
	@ApiOperation(value= "Update brand")
	@RequestMapping(path= "/api/brand/update/{id}", method = RequestMethod.PUT)
	public void updateBrand(@PathVariable int id, @RequestBody BrandForm b) throws ApiException{
		brandFlow.updateBrand(id, b);
	}
	
	@ApiOperation(value= "Get categories from brand")
	@RequestMapping(path= "/api/brand/get-categories/{brandName}", method = RequestMethod.GET)
	public List<String> getCategoriesFromBrand(@PathVariable String brandName) throws ApiException{
		return brandFlow.getCategoriesByBrand(brandName);
	}
	
}

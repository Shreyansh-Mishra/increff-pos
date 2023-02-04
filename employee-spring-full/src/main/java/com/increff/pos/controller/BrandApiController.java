package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class BrandApiController {
	@Autowired
	private BrandDto brandDto;
	
	
	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/brand", method = RequestMethod.POST)
	public void createBrand(@RequestBody BrandForm form) throws ApiException {
		brandDto.createBrand(form);
	}
	
	@ApiOperation(value= "Get all brands")
	@RequestMapping(path= "/brands", method = RequestMethod.GET)
	public List<BrandData> getBrands(){
		return brandDto.getAllBrands();
	}
	
	@ApiOperation(value= "Get brand by name and category")
	@RequestMapping(path= "/brand/{name}/{category}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable String name,@PathVariable String category) throws ApiException{
		return brandDto.getBrandByNameAndCategory(name, category);
	}
	
	@ApiOperation(value= "Get brand by id")
	@RequestMapping(path= "/brand/{id}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable int id) throws ApiException{
		return brandDto.getBrandById(id);
	}

	@ApiOperation(value= "Get categories from brand")
	@RequestMapping(path= "/brand/get-categories/{brandName}", method = RequestMethod.GET)
	public List<String> getCategoriesFromBrand(@PathVariable String brandName){
		return brandDto.getCategoriesByBrand(brandName);
	}

	@ApiOperation(value= "Update brand")
	@RequestMapping(path= "/brand/{id}", method = RequestMethod.PUT)
	public void updateBrand(@PathVariable int id, @RequestBody BrandForm b) throws ApiException{
		brandDto.updateBrand(id, b);
	}
}

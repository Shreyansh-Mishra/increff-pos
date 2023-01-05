package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
	@Autowired
	private BrandService brandService;
	
	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/api/brand/add-brand", method = RequestMethod.POST)
	public void addBrand(@RequestBody BrandForm form) throws ApiException {
		BrandPojo b = convert(form);
		brandService.add(b);
	}
	
	@ApiOperation(value= "Get all brands")
	@RequestMapping(path= "/api/brand/get-brands", method = RequestMethod.GET)
	public List<BrandData> getBrands(){
		List<BrandPojo> b = brandService.selectAll();
		List<BrandData> b2 = new ArrayList<BrandData>();
		for(BrandPojo i: b) {
			b2.add(convert(i));
		}
		return b2;
	}
	
	@ApiOperation(value= "Get brand by name and category")
	@RequestMapping(path= "/api/brand/get-brand/{name}/{category}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable String name,@PathVariable String category) throws ApiException{
		BrandPojo b = brandService.selectByNameAndCategory(name, category);
		BrandData b2 = convert(b);
		return b2;
	}
	
	
	private static BrandPojo convert(BrandForm f) {
		BrandPojo b = new BrandPojo();
		b.setBrand(f.getBrand());
		b.setCategory(f.getCategory());
		return b;
	}
	
	private static BrandData convert(BrandPojo b) {
		BrandData b2 = new BrandData();
		b2.setBrand(b.getBrand());
		b2.setCategory(b.getCategory());
		b2.setId(b.getId());
		return b2;
	}
	
}

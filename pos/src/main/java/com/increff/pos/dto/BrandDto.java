package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ObjectUtil;

@Component
public class BrandDto {
	@Autowired
	private BrandService brandService;
	
	public BrandData createBrand(BrandForm form) throws ApiException {
		//convert form to pojo
		BrandPojo brand = ObjectUtil.objectMapper(form, BrandPojo.class);
		//add brand and return brand data
		return ObjectUtil.objectMapper(brandService.add(brand), BrandData.class);
	}
	
	public List<BrandData> getAllBrands() {
		//get all brands
		List<BrandPojo> brands = brandService.selectAll();
		List<BrandData> brandData = new ArrayList<>();
		//convert pojo to data
		for(BrandPojo i: brands) {
			brandData.add(ObjectUtil.objectMapper(i,BrandData.class));
		}
		//return brand data
		return brandData;
	}
	
	public BrandData getBrandByNameAndCategory(String name, String category) throws ApiException {
		//get brand by name and category
		BrandPojo brand = brandService.selectByNameAndCategory(name.toLowerCase(), category.toLowerCase());
		//convert pojo to data
		return ObjectUtil.objectMapper(brand, BrandData.class);
	}
	
	public BrandData getBrandById(Integer id) throws ApiException {
		//get brand by id
		BrandPojo brand = brandService.selectById(id);
		//convert pojo to data
		return ObjectUtil.objectMapper(brand, BrandData.class);
	}
	
	public void updateBrand(Integer id, BrandForm brandForm) throws ApiException {
		//convert form to pojo
		BrandPojo brand = ObjectUtil.objectMapper(brandForm, BrandPojo.class);
		//update brand
		brandService.updateBrand(id,brand);
	}
	
	public List<String> getCategoriesByBrand(String brandName) {
		return brandService.getCategories(brandName);
	}

}

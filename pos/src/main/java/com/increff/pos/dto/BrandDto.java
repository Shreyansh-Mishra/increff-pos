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
		BrandPojo brand = ObjectUtil.objectMapper(form, BrandPojo.class);
		return ObjectUtil.objectMapper(brandService.add(brand), BrandData.class);
	}
	
	public List<BrandData> getAllBrands() {
		List<BrandPojo> brands = brandService.selectAll();
		List<BrandData> brandData = new ArrayList<>();
		for(BrandPojo i: brands) {
			brandData.add(ObjectUtil.objectMapper(i,BrandData.class));
		}
		return brandData;
	}
	
	public BrandData getBrandByNameAndCategory(String name, String category) throws ApiException {
		BrandPojo brand = brandService.selectByNameAndCategory(name.toLowerCase(), category.toLowerCase());
		return ObjectUtil.objectMapper(brand, BrandData.class);
	}
	
	public BrandData getBrandById(Integer id) throws ApiException {
		BrandPojo brand = brandService.selectById(id);
		return ObjectUtil.objectMapper(brand, BrandData.class);
	}
	
	public void updateBrand(Integer id, BrandForm brandForm) throws ApiException {
		BrandPojo brand = ObjectUtil.objectMapper(brandForm, BrandPojo.class);
		brandService.updateBrand(id,brand);
	}
	
	public List<String> getCategoriesByBrand(String brandName) {
		return brandService.getCategories(brandName);
	}

}

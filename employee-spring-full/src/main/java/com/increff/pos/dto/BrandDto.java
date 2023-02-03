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
import com.increff.pos.util.DtoUtil;

@Component
public class BrandDto {
	@Autowired
	BrandService brandService;
	
	public void createBrand(BrandForm form) throws ApiException {
		BrandPojo b = DtoUtil.objectMapper(form, BrandPojo.class);
		brandService.add(b);
	}
	
	public List<BrandData> getAllBrands() {
		List<BrandPojo> b = brandService.selectAll();
		List<BrandData> b2 = new ArrayList<>();
		for(BrandPojo i: b) {
			b2.add(DtoUtil.objectMapper(i,BrandData.class));
		}
		return b2;
	}
	
	public BrandData getBrandByNameAndCategory(String name, String category) throws ApiException {
		BrandPojo b = brandService.selectByNameAndCategory(name.toLowerCase(), category.toLowerCase());
		BrandData b2 = DtoUtil.objectMapper(b, BrandData.class);
		return b2;
	}
	
	public BrandData getBrandById(int id) throws ApiException {
		BrandPojo b = brandService.selectById(id);
		BrandData b2 = DtoUtil.objectMapper(b, BrandData.class);
		return b2;
	}
	
	public void updateBrand(int id, BrandForm b) throws ApiException {
		BrandPojo b2 = DtoUtil.objectMapper(b, BrandPojo.class);
		brandService.updateBrand(id,b2);
	}
	
	public List<String> getCategoriesByBrand(String brandName) {
		return brandService.getCategories(brandName);
	}

}

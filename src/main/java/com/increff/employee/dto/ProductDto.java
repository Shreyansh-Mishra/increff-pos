package com.increff.employee.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.ProductData;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;

@Component
public class ProductDto {
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;
	
	public ProductData convert(ProductData p, ProductPojo p2) throws ApiException {
		BrandPojo b = brandService.selectById(p2.getBrand_category());
		p.setBrandName(b.getBrand());
		p.setCategory(b.getCategory());
		return p;
	}
}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

import javax.transaction.Transactional;

@Component
public class ProductDto {
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;

	@Transactional(rollbackOn = ApiException.class)
	public void createProduct(ProductForm form) throws ApiException {
		ProductPojo p2 = convert(form);
		BrandPojo b = brandService.selectByNameAndCategory(p2.getBrandName().toLowerCase(),p2.getCategory().toLowerCase());
		p2.setBrand_category(b.getId());
		productService.add(p2);
	}
	
	public List<ProductData> getAllProducts() throws ApiException {
		List<ProductPojo> products = productService.selectAll();
		List<ProductData> productList = new ArrayList<ProductData>();
		for(ProductPojo product: products) {
			ProductData data = convert(product);
			data = convert(data,product);
			productList.add(data);
		}
		return productList;
	}
	
	public ProductData getProductsById(int id) throws ApiException{
		ProductPojo p = productService.selectById(id);
		ProductData p2 = convert(p);
		return p2;
	}
	
	public List<ProductData> getProductByBrandName(String brandName) throws ApiException{
		List<ProductPojo> products = productService.selectByBrand(brandName);
		List<ProductData> productList = new ArrayList<ProductData>();
		for(ProductPojo product: products) {
			productList.add(convert(product));
		}
		return productList;
	}
	
	public List<ProductData> getProductsByBrandAndCategory(String brandName, String category) throws ApiException{
		List<ProductPojo> products = productService.selectByBrandAndCategory(brandName,category);
		List<ProductData> productList = new ArrayList<ProductData>();
		for(ProductPojo product: products) {
			productList.add(convert(product));
		}
		return productList;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateProduct(int id, ProductForm form) throws ApiException {
		ProductPojo p2 = convert(form);
		BrandPojo b = brandService.selectByNameAndCategory(p2.getBrandName().toLowerCase(),p2.getCategory().toLowerCase());
		p2.setBrand_category(b.getId());
		productService.update(p2,id);
	}
	
	private static ProductPojo convert(ProductForm p) {
		ProductPojo p2 = new ProductPojo();
		p2.setBarcode(p.getBarcode());
		p2.setName(p.getName());
		p2.setMrp(p.getMrp());
		p2.setBrandName(p.getBrandName());
		p2.setCategory(p.getCategory());
		return p2;
	}
	
	private static ProductData convert(ProductPojo p) {
		ProductData p2 = new ProductData();
		p2.setBarcode(p.getBarcode());
		p2.setId(p.getId());
		p2.setMrp(p.getMrp());
		p2.setName(p.getName());
		return p2;
	}
	
	public ProductData convert(ProductData p, ProductPojo p2) throws ApiException {
		BrandPojo b = brandService.selectById(p2.getBrand_category());
		p.setBrandName(b.getBrand());
		p.setCategory(b.getCategory());
		return p;
	}


}

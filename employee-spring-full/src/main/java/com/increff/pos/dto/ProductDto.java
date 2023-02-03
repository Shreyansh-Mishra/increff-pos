package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.DtoUtil;
import com.increff.pos.util.StringUtil;
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
		ProductPojo p2 = DtoUtil.objectMapper(form, ProductPojo.class);
		BrandPojo b = brandService.selectByNameAndCategory(p2.getBrandName().toLowerCase(),p2.getCategory().toLowerCase());
		p2.setBrand_category(b.getId());
		productService.add(p2);
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<ProductData> getAllProducts() throws ApiException {
		List<ProductPojo> products = productService.selectAll();
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			ProductData data = DtoUtil.objectMapper(product,ProductData.class);
			BrandPojo brand = brandService.selectById(product.getBrand_category());
			data.setBrandName(brand.getBrand());
			data.setCategory(brand.getCategory());
			productList.add(data);
		}
		return productList;
	}
	
	public ProductData getProductsById(int id) throws ApiException{
		ProductPojo p = productService.selectById(id);
		ProductData p2 = DtoUtil.objectMapper(p,ProductData.class);
		return p2;
	}
	
	public List<ProductData> getProductByBrandName(String brandName){
		List<ProductPojo> products = productService.selectByBrand(brandName);
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			productList.add(DtoUtil.objectMapper(product,ProductData.class));
		}
		return productList;
	}
	
	public List<ProductData> getProductsByBrandAndCategory(String brandName, String category){
		List<ProductPojo> products = productService.selectByBrandAndCategory(brandName,category);
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			productList.add(DtoUtil.objectMapper(product,ProductData.class));
		}
		return productList;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateProduct(int id, ProductForm form) throws ApiException {
		ProductPojo p2 = DtoUtil.objectMapper(form, ProductPojo.class);
		BrandPojo b = brandService.selectByNameAndCategory(p2.getBrandName().toLowerCase(),p2.getCategory().toLowerCase());
		p2.setBrand_category(b.getId());
		productService.update(p2,id);
	}

	public ProductData getProductByBarcode(String barcode) throws ApiException {
		ProductPojo p = productService.selectByBarcode(barcode);
		ProductData p2 = DtoUtil.objectMapper(p,ProductData.class);
		return p2;
	}
}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.DtoUtil;
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
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;

	@Transactional(rollbackOn = ApiException.class)
	public void createProduct(ProductForm form) throws ApiException {
		ProductPojo product = DtoUtil.objectMapper(form, ProductPojo.class);
		BrandPojo brand = brandService.selectByNameAndCategory(product.getBrandName().toLowerCase(),product.getCategory().toLowerCase());
		product.setBrand_category(brand.getId());
		productService.add(product);
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
		return DtoUtil.objectMapper(p,ProductData.class);
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
		ProductPojo product = DtoUtil.objectMapper(form, ProductPojo.class);
		BrandPojo brand = brandService.selectByNameAndCategory(product.getBrandName().toLowerCase(),product.getCategory().toLowerCase());
		product.setBrand_category(brand.getId());
		productService.update(product,id);
	}

	public ProductData getProductByBarcode(String barcode) throws ApiException {
		ProductPojo p = productService.selectByBarcode(barcode);
		return DtoUtil.objectMapper(p,ProductData.class);
	}
}

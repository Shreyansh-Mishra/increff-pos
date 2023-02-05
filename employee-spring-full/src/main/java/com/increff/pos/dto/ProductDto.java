package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.ObjectUtil;
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
	public ProductData createProduct(ProductForm form) throws ApiException {
		ProductPojo product = ObjectUtil.objectMapper(form, ProductPojo.class);
		BrandPojo brand = brandService.selectByNameAndCategory(product.getBrandName().toLowerCase(),product.getCategory().toLowerCase());
		product.setBrand_category(brand.getId());
		return ObjectUtil.objectMapper(productService.add(product), ProductData.class);
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<ProductData> getAllProducts() throws ApiException {
		List<ProductPojo> products = productService.selectAll();
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			ProductData data = ObjectUtil.objectMapper(product,ProductData.class);
			BrandPojo brand = brandService.selectById(product.getBrand_category());
			data.setBrandName(brand.getBrand());
			data.setCategory(brand.getCategory());
			productList.add(data);
		}
		return productList;
	}
	
	public ProductData getProductsById(Integer id) throws ApiException{
		ProductPojo p = productService.selectById(id);
		return ObjectUtil.objectMapper(p,ProductData.class);
	}
	
	public List<ProductData> getProductByBrandName(String brandName){
		List<ProductPojo> products = productService.selectByBrand(brandName);
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			productList.add(ObjectUtil.objectMapper(product,ProductData.class));
		}
		return productList;
	}
	
	public List<ProductData> getProductsByBrandAndCategory(String brandName, String category){
		List<ProductPojo> products = productService.selectByBrandAndCategory(brandName,category);
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			productList.add(ObjectUtil.objectMapper(product,ProductData.class));
		}
		return productList;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateProduct(Integer id, ProductForm form) throws ApiException {
		ProductPojo product = ObjectUtil.objectMapper(form, ProductPojo.class);
		BrandPojo brand = brandService.selectByNameAndCategory(product.getBrandName().toLowerCase(),product.getCategory().toLowerCase());
		product.setBrand_category(brand.getId());
		productService.update(product,id);
	}

	public ProductData getProductByBarcode(String barcode) throws ApiException {
		ProductPojo p = productService.selectByBarcode(barcode);
		return ObjectUtil.objectMapper(p,ProductData.class);
	}
}

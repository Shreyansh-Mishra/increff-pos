package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.flow.ProductFlow;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api
@RestController
public class ProductApiController {
	
	@Autowired
	private ProductFlow productFlow;
	
	
	@ApiOperation("Adds a Product")
	@RequestMapping(path="/api/product/add-product", method=RequestMethod.POST)
	public void createProduct(@RequestBody ProductForm p) throws ApiException {
		productFlow.createProduct(p);
	}
	
	@ApiOperation("Get All Products")
	@RequestMapping(path="/api/product/get-products", method=RequestMethod.GET)
	public List<ProductData> getProducts() throws ApiException{
		return productFlow.getAllProducts();
	}
	
	@ApiOperation("Get Products by id")
	@RequestMapping(path="/api/product/{id}", method=RequestMethod.GET)
	public ProductData getProductById(@PathVariable int id) throws ApiException {
		return productFlow.getProductsById(id);
	}
	
	@ApiOperation("Get Products by brand name")
	@RequestMapping(path="/api/product/get-product-brand/{brandName}", method=RequestMethod.GET)
	public List<ProductData> getProductsByBrand(@PathVariable String brandName) throws ApiException{
		return productFlow.getProductByBrandName(brandName);
	}
	
	@ApiOperation("Get Products by Brand and Category")
	@RequestMapping(path="/api/product/get-product/{brand}/{category}", method=RequestMethod.GET)
	public List<ProductData> getProductsByCategory(@PathVariable String brand,@PathVariable String category) throws ApiException{
		return productFlow.getProductsByBrandAndCategory(brand, category);
	}
	
	@ApiOperation("Edit Product")
	@RequestMapping(path="/api/product/{id}", method=RequestMethod.PUT)
	public void updateProduct(@PathVariable int id, @RequestBody ProductForm p) throws ApiException{
		productFlow.updateProduct(id, p);
	}
}

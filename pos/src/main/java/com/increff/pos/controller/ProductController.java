package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api
@RestController
@RequestMapping(path = "/api")
public class ProductController {
	
	@Autowired
	private ProductDto productDto;
	
	
	@ApiOperation("Adds a Product")
	@RequestMapping(path="/products", method=RequestMethod.POST)
	public ProductData createProduct(@RequestBody ProductForm p) throws ApiException {
		return productDto.createProduct(p);
	}
	
	@ApiOperation("Get All Products")
	@RequestMapping(path="/products", method=RequestMethod.GET)
	public List<ProductData> getProducts() throws ApiException{
		return productDto.getAllProducts();
	}
	
	@ApiOperation("Get Products by id")
	@RequestMapping(path="/products/{id}", method=RequestMethod.GET)
	public ProductData getProductById(@PathVariable Integer id) throws ApiException {
		return productDto.getProductsById(id);
	}

	@ApiOperation("Edit Product")
	@RequestMapping(path="/products/{id}", method=RequestMethod.PUT)
	public void updateProduct(@PathVariable Integer id, @RequestBody ProductForm product) throws ApiException{
		productDto.updateProduct(id, product);
	}

	@ApiOperation("Get Product By Barcode")
	@RequestMapping(path="/products/barcode/{barcode}", method=RequestMethod.GET)
	public ProductData getProductByBarcode(@PathVariable String barcode) throws ApiException{
		return productDto.getProductByBarcode(barcode);
	}
}

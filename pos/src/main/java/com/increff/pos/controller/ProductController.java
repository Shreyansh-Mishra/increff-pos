package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.model.EditProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api
@RestController
@ControllerAdvice
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
	public void updateProduct(@PathVariable Integer id, @RequestBody EditProductForm product) throws ApiException{
		productDto.updateProduct(id, product);
	}

	@ApiOperation("Get Product By Barcode")
	@RequestMapping(path="/products/barcode/{barcode}", method=RequestMethod.GET)
	public ProductData getProductByBarcode(@PathVariable String barcode) throws ApiException{
		return productDto.getProductByBarcode(barcode);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
	}
}

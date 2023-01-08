package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {
	@Autowired
	private ProductService productService;
	
	@ApiOperation("Adds a Product")
	@RequestMapping(path="/api/product/add-product", method=RequestMethod.POST)
	public void addProduct(@RequestBody ProductForm p) throws ApiException {
		ProductPojo p2 = convert(p);
		productService.add(p2);
	}
	
	@ApiOperation("Get All Products")
	@RequestMapping(path="/api/product/get-products", method=RequestMethod.GET)
	public List<ProductData> getProducts(){
		List<ProductPojo> p = productService.getAll();
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			p2.add(convert(i));
		}
		return p2;
	}
	
	@ApiOperation("Get Products by brand name")
	@RequestMapping(path="/api/product/{id}", method=RequestMethod.GET)
	public ProductData getProductById(@PathVariable int id) throws ApiException {
		ProductPojo p = productService.getById(id);
		ProductData p2 = convert(p);
		return p2;
	}
	
	@ApiOperation("Get Products by brand name")
	@RequestMapping(path="/api/product/get-product-brand/{brandName}", method=RequestMethod.GET)
	public List<ProductData> getProductsByBrand(@PathVariable String brandName) throws ApiException{
		List<ProductPojo> p = productService.getByBrand(brandName);
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			p2.add(convert(i));
		}
		return p2;
	}
	
	@ApiOperation("Get Products by Brand and Category")
	@RequestMapping(path="/api/product/get-product/{brand}/{category}", method=RequestMethod.GET)
	public List<ProductData> getProductsByCategory(@PathVariable String brand,@PathVariable String category) throws ApiException{
		List<ProductPojo> p = productService.getByBrandAndCategory(brand,category);
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			p2.add(convert(i));
		}
		return p2;
	}
	
	@ApiOperation("Delete Product")
	@RequestMapping(path="/api/product/delete-product/{id}", method=RequestMethod.DELETE)
	public void getProductsByCategory(@PathVariable int id) throws ApiException{
		productService.deleteProduct(id);
	}
	
	@ApiOperation("Update Product")
	@RequestMapping(path="/api/product/update-product/{id}", method=RequestMethod.PUT)
	public void getProductsByCategory(@PathVariable int id, @RequestBody ProductForm p) throws ApiException{
		ProductPojo p2 = convert(p);
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
		p2.setBrand_category(p.getBrand_category());
		p2.setId(p.getId());
		p2.setMrp(p.getMrp());
		p2.setName(p.getName());
		return p2;
	}
}

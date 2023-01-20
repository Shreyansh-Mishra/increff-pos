package com.increff.employee.flow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;

@Component
public class ProductFlow {
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;
	
	public void createProduct(ProductForm form) throws ApiException {
		normalize(form);
		if(form.getName().isEmpty()||form.getBarcode().isEmpty()) {
			throw new ApiException("Please fill all the fields!");
		}
		if(isNegative(form.getMrp())) {
			throw new ApiException("Enter a Valid MRP");
		}
		ProductPojo p2 = convert(form);
		productService.add(p2);
	}
	
	public List<ProductData> getAllProducts() throws ApiException {
		List<ProductPojo> p = productService.selectAll();
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			ProductData p3 = convert(i);
			p3 = convert(p3,i);
			p2.add(p3);
		}
		return p2;
	}
	
	public ProductData getProductsById(int id) throws ApiException{
		ProductPojo p = productService.selectById(id);
		ProductData p2 = convert(p);
		return p2;
	}
	
	public List<ProductData> getProductByBrandName(String brandName) throws ApiException{
		List<ProductPojo> p = productService.selectByBrand(brandName);
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			p2.add(convert(i));
		}
		return p2;
	}
	
	public List<ProductData> getProductsByBrandAndCategory(String brandName, String category) throws ApiException{
		List<ProductPojo> p = productService.selectByBrandAndCategory(brandName,category);
		List<ProductData> p2 = new ArrayList<ProductData>();
		for(ProductPojo i: p) {
			p2.add(convert(i));
		}
		return p2;
	}
	
	public void updateProduct(int id, ProductForm form) throws ApiException {
		if(form.getName().isEmpty()||form.getBarcode().isEmpty()) {
			throw new ApiException("Please fill all the fields!");
		}
		if(isNegative(form.getMrp())) {
			throw new ApiException("Enter a Valid MRP");
		}
		normalize(form);
		ProductPojo p2 = convert(form);
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
	
	public static boolean isNegative(double num) {
		if(num<=0)
			return true;
		return false;
	}

	protected static void normalize(ProductForm p) {
		p.setName(p.getName().toLowerCase().trim());
		p.setBarcode(p.getBarcode().toLowerCase().trim());
		p.setBrandName(p.getBrandName().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}
}

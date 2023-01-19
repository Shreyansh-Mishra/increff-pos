package com.increff.employee.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private BrandDao brandDao;
	
	@Transactional
	public void add(ProductPojo p) throws ApiException {
		ProductPojo p2 = productDao.selectBarcode(p.getBarcode());
		if(p2!=null) {
			throw new ApiException("The product already exists");
		}
		BrandPojo b = brandDao.select(p.getBrandName(),p.getCategory());
		if(b!=null) {
			p.setBrand_category(b.getId());
			productDao.insert(p);
		}
		else {
			throw new ApiException("The entered brand and category combination does not exists");
		}
	}
	
	@Transactional
	public List<ProductPojo> selectAll() {
		List<ProductPojo> p = productDao.selectAll();
		return p;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> selectByBrand(String brandName) throws ApiException{
		List<ProductPojo> p2 = productDao.selectBrand(brandName.toLowerCase());
		if(p2.isEmpty()) {
			throw new ApiException("No product of this Brand available");
		}
		return p2;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo selectById(int id) throws ApiException {
		ProductPojo p = productDao.selectId(id);
		if(p==null)
			throw new ApiException("The product does not exists");
		return p;
	}

	
	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> selectByBrandAndCategory(String brand, String category) throws ApiException{
		List<ProductPojo> p2 = productDao.selectBrandAndCategory(brand.toLowerCase(), category.toLowerCase());
		if(p2.isEmpty()) {
			throw new ApiException("No product of this Brand and Category found!");
		}
		return p2;
	}

	@Transactional
	public void update(ProductPojo p,int id) throws ApiException {
		ProductPojo p2 = productDao.selectBarcode(p.getBarcode());
		if(p2!=null && p2.getId()!=id) {
			throw new ApiException("A Product already exists with the same barcode!");
		}
		
		p2 = productDao.selectId(id); 
		if(p2==null) {
			throw new ApiException("The Product does not exists");
		}
		
		BrandPojo b = brandDao.select(p.getBrandName(),p.getCategory());
		if(b==null) {
			throw new ApiException("The Requested Brand and Category combination does not exists");
		}
		p2.setBarcode(p.getBarcode());
		p2.setBrandName(p.getBrandName());
		p2.setMrp(p.getMrp());
		p2.setName(p.getName());
		p2.setBrand_category(b.getId());
		productDao.update();
	}

}

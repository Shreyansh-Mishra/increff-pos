package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	
	@Transactional
	public void add(ProductPojo p) throws ApiException {
		ProductPojo p2 = productDao.selectBarcode(p.getBarcode());
		if(p2!=null) {
			throw new ApiException("The product already exists");
		}
		productDao.insert(p);
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
		p2.setBarcode(p.getBarcode());
		p2.setBrandName(p.getBrandName());
		p2.setMrp(p.getMrp());
		p2.setName(p.getName());
		productDao.update();
	}

}

package com.increff.employee.service;

import java.util.List;

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
	public List<ProductPojo> getAll() {
		List<ProductPojo> p = productDao.selectAll();
		return p;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getByBrand(String brandName) throws ApiException{
		List<ProductPojo> p2 = productDao.selectBrand(brandName);
		if(p2.isEmpty()) {
			throw new ApiException("No product of this Brand available");
		}
		return p2;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getByCategory(String category) throws ApiException{
		List<ProductPojo> p2 = productDao.selectCategory(category);
		if(p2.isEmpty()) {
			throw new ApiException("No Product of this Category found");
		}
		return p2;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getByBrandAndCategory(String brand,String category) throws ApiException{
		List<ProductPojo> p2 = productDao.selectBrandAndCategory(brand, category);
		if(p2.isEmpty()) {
			throw new ApiException("No product of this Brand and Category found!");
		}
		return p2;
	}
	
	@Transactional
	public void deleteProduct(int id) {
		productDao.delete(id);
	}
	
	@Transactional
	public void update(ProductPojo p,int id) throws ApiException {
		ProductPojo p2 = productDao.selectId(id);
		BrandPojo b = brandDao.select(p.getBrandName(),p.getCategory());
		if(p2==null) {
			throw new ApiException("The Product does not exists");
		}
		else if(b==null) {
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

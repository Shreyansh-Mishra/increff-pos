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
	
	public List<ProductPojo> getAll() {
		List<ProductPojo> p = productDao.selectAll();
		return p;
	}
	
	public List<ProductPojo> getByBrand(String brandName){
		List<ProductPojo> p2 = productDao.selectBrand(brandName);
		return p2;
	}
}

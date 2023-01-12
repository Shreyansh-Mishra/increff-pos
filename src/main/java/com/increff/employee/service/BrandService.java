package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;

@Service
public class BrandService {
	@Autowired
	private BrandDao brandDao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo b) throws ApiException {
		normalize(b);
		BrandPojo existing = brandDao.select(b.getBrand(),b.getCategory());
		if(existing!=null) {
			throw new ApiException("Brand and Category Already Exists");
		}
		else {
			brandDao.insert(b);
		}
	}
	
	@Transactional
	public List<BrandPojo> selectAll(){
		return brandDao.selectAll();
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo selectByNameAndCategory(String name, String category) throws ApiException {
		BrandPojo b = brandDao.select(name, category);
		if(b==null) {
			throw new ApiException("The requested brand and category combination does not exists");
		}
		return b;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo selectById(int id) throws ApiException {
		BrandPojo b = brandDao.select(id);
		if(b==null) {
			throw new ApiException("The requested brand and category combination does not exists");
		}
		return b;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<BrandPojo> selectByName(String name) throws ApiException {
		List<BrandPojo> b = brandDao.select(name);
		if(b.isEmpty()) {
			throw new ApiException("The requested brand does not exists");
		}
		return b;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void updateBrand(int id, BrandPojo b) throws ApiException {
		BrandPojo b2 = checkIfExists(id);
		BrandPojo b3 = brandDao.select(b.getBrand(), b.getCategory());
		if(b3==null) {
			b2.setBrand(b.getBrand());
			b2.setCategory(b.getCategory());
			brandDao.update(b2);
		}
		else {
			throw new ApiException("The brand and category already exists!");
		}
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo checkIfExists(int id) throws ApiException {
		BrandPojo b = brandDao.select(id);
		if(b==null) {
			throw new ApiException("The requested brand does not exists");
		}
		return b;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<String> getCategories(String brandName) {
		return brandDao.selectCategories(brandName);
	}
	
	
	public static void normalize(BrandPojo b) {
		b.setBrand(b.getBrand().toLowerCase().trim());
		b.setCategory(b.getCategory().toLowerCase().trim());
	}
	
	
}

package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.model.BrandForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {
	@Autowired
	private BrandDao brandDao;

	public void add(BrandPojo brand) throws ApiException {
		isEmpty(brand.getBrand(),brand.getCategory());
		normalize(brand);
		BrandPojo isExists = brandDao.select(brand.getBrand(),brand.getCategory());
		if(isExists!=null) {
			throw new ApiException("Brand and Category Already Exists");
		}
		brandDao.insert(brand);
	}
	
	public List<BrandPojo> selectAll(){
		return brandDao.selectAll();
	}
	
	public BrandPojo selectByNameAndCategory(String name, String category) throws ApiException {
		BrandPojo brand = brandDao.select(name, category.toLowerCase());
		if(brand==null) {
			throw new ApiException("The requested brand and category combination does not exists");
		}
		return brand;
	}
	
	public BrandPojo selectById(int id) throws ApiException {
		return checkIfExists(id);
	}
	
	public void updateBrand(int id, BrandPojo brand) throws ApiException {
		isEmpty(brand.getBrand(),brand.getCategory());
		normalize(brand);
		BrandPojo newBrand = checkIfExists(id);
		BrandPojo isExist = brandDao.select(brand.getBrand(), brand.getCategory());
		if(isExist!=null){
			throw new ApiException("The Brand and Category already exists!");
		}
		newBrand.setBrand(brand.getBrand());
		newBrand.setCategory(brand.getCategory());
		brandDao.update(newBrand);
	}
	
	public BrandPojo checkIfExists(int id) throws ApiException {
		BrandPojo brand = brandDao.select(id);
		if(brand==null) {
			throw new ApiException("The requested brand does not exists");
		}
		return brand;
	}
	
	public List<String> getCategories(String brandName) {
		return brandDao.selectCategories(brandName);
	}

	public static void isEmpty(String a, String b) throws ApiException {
		if(a.isEmpty()||b.isEmpty()) {
			throw new ApiException("Brand or Category cannot be empty!");
		}
	}

	public static void normalize(BrandPojo b) {
		b.setBrand(b.getBrand().toLowerCase().trim());
		b.setCategory(b.getCategory().toLowerCase().trim());
	}
}

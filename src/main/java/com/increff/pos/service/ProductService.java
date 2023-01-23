package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;


import com.increff.pos.model.ProductForm;
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
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo product) throws ApiException {
		if(product.getName().isEmpty()||product.getBarcode().isEmpty()) {
			throw new ApiException("Please fill all the fields!");
		}
		if(isNegative(product.getMrp())) {
			throw new ApiException("Enter a Valid MRP");
		}
		normalize(product);
		ProductPojo p2 = productDao.selectBarcode(product.getBarcode());
		if(p2!=null) {
			throw new ApiException("The product already exists");
		}

		productDao.insert(product);
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

	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo selectByBarcode(String barcode) throws ApiException {
		ProductPojo p = productDao.selectBarcode(barcode);
		if(p==null)
			throw new ApiException("The product with barcode "+ barcode +" does not exists");
		return p;
	}

	@Transactional
	public void update(ProductPojo product,int id) throws ApiException {
		if(product.getName().isEmpty()||product.getBarcode().isEmpty()) {
			throw new ApiException("Please fill all the fields!");
		}
		if(isNegative(product.getMrp())) {
			throw new ApiException("Enter a Valid MRP");
		}
		normalize(product);
		ProductPojo p2 = productDao.selectBarcode(product.getBarcode());
		if(p2!=null && p2.getId()!=id) {
			throw new ApiException("A Product already exists with the same barcode!");
		}
		
		p2 = productDao.selectId(id);
		if(p2==null) {
			throw new ApiException("The Product does not exists");
		}
		p2.setBarcode(product.getBarcode());
		p2.setBrandName(product.getBrandName());
		p2.setMrp(product.getMrp());
		p2.setName(product.getName());
		productDao.update();
	}

	public static boolean isNegative(double num) {
		if(num<=0)
			return true;
		return false;
	}

	protected static void normalize(ProductPojo p) {
		p.setName(p.getName().toLowerCase().trim());
		p.setBarcode(p.getBarcode().toLowerCase().trim());
		p.setBrandName(p.getBrandName().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}

}

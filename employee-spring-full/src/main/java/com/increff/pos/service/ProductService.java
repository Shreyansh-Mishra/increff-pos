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
@Transactional(rollbackOn = ApiException.class)
public class ProductService {
	@Autowired
	private ProductDao productDao;

	public void add(ProductPojo product) throws ApiException {
		isEmpty(product.getName(),product.getBarcode());
		isNegative(product.getMrp());
		normalize(product);
		ProductPojo isExists = productDao.selectBarcode(product.getBarcode());
		if(isExists!=null) {
			throw new ApiException("The product already exists");
		}
		productDao.insert(product);
	}
	
	public List<ProductPojo> selectAll() {
		return productDao.selectAll();
	}
	
	public List<ProductPojo> selectByBrand(String brandName){
		return productDao.selectBrand(brandName.toLowerCase());
	}
	
	public ProductPojo selectById(int id) throws ApiException {
		ProductPojo product = productDao.selectId(id);
		if(product==null)
			throw new ApiException("The product with id "+ id +" does not exists");
		return product;
	}

	
	public List<ProductPojo> selectByBrandAndCategory(String brand, String category){
		return productDao.selectBrandAndCategory(brand.toLowerCase(), category.toLowerCase());
	}

	public ProductPojo selectByBarcode(String barcode) throws ApiException {
		isEmpty(barcode,barcode);
		ProductPojo product = productDao.selectBarcode(barcode.trim().toLowerCase());
		if(product==null)
			throw new ApiException("The product with barcode "+ barcode +" does not exists");
		return product;
	}


	public void update(ProductPojo product,int id) throws ApiException {
		isEmpty(product.getName(),product.getBarcode());
		isNegative(product.getMrp());
		normalize(product);
		ProductPojo prod2 = productDao.selectBarcode(product.getBarcode());
		if(prod2!=null && prod2.getId()!=id) {
			throw new ApiException("A Product already exists with the same barcode!");
		}
		prod2 = this.selectById(id);
		prod2.setBarcode(product.getBarcode());
		prod2.setBrand_category(product.getBrand_category());
		prod2.setMrp(product.getMrp());
		prod2.setName(product.getName());
		productDao.update();
	}

	public static void isNegative(double num) throws ApiException {
		if(num<=0)
			throw new ApiException("Enter a Valid MRP");
	}

	public static void isEmpty(String a, String b) throws ApiException {
		if(a.isEmpty()||b.isEmpty())
			throw new ApiException("Please fill all the fields!");
	}

	protected static void normalize(ProductPojo p) {
		p.setName(p.getName().toLowerCase().trim());
		p.setBarcode(p.getBarcode().toLowerCase().trim());
		p.setBrandName(p.getBrandName().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}

}

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
		ProductPojo p2 = productDao.selectBarcode(product.getBarcode());
		if(p2!=null) {
			throw new ApiException("The product already exists");
		}

		productDao.insert(product);
	}
	
	public List<ProductPojo> selectAll() {
		List<ProductPojo> p = productDao.selectAll();
		return p;
	}
	
	public List<ProductPojo> selectByBrand(String brandName){
		List<ProductPojo> p2 = productDao.selectBrand(brandName.toLowerCase());
		return p2;
	}
	
	public ProductPojo selectById(int id) throws ApiException {
		ProductPojo p = productDao.selectId(id);
		if(p==null)
			throw new ApiException("The product with id "+ id +" does not exists");
		return p;
	}

	
	public List<ProductPojo> selectByBrandAndCategory(String brand, String category){
		List<ProductPojo> p2 = productDao.selectBrandAndCategory(brand.toLowerCase(), category.toLowerCase());
		return p2;
	}

	public ProductPojo selectByBarcode(String barcode) throws ApiException {
		isEmpty(barcode,barcode);
		ProductPojo p = productDao.selectBarcode(barcode.trim().toLowerCase());
		if(p==null)
			throw new ApiException("The product with barcode "+ barcode +" does not exists");
		return p;
	}


	public void update(ProductPojo product,int id) throws ApiException {
		isEmpty(product.getName(),product.getBarcode());
		isNegative(product.getMrp());
		normalize(product);
		ProductPojo p2 = productDao.selectBarcode(product.getBarcode());
		if(p2!=null && p2.getId()!=id) {
			throw new ApiException("A Product already exists with the same barcode!");
		}
		p2 = this.selectById(id);
		p2.setBarcode(product.getBarcode());
		p2.setBrand_category(product.getBrand_category());
		p2.setMrp(product.getMrp());
		p2.setName(product.getName());
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

package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;


import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {
	@Autowired
	private ProductDao productDao;

	public ProductPojo add(ProductPojo product) throws ApiException {
		isEmpty(product.getName(),product.getBarcode());
		isNegative(product.getMrp());
		normalize(product);
		ProductPojo isExists = productDao.selectBarcode(product.getBarcode());
		if(isExists!=null) {
			throw new ApiException("The product already exists");
		}
		return productDao.insert(product);
	}
	
	public List<ProductPojo> selectAll() {
		return productDao.selectAll();
	}
	
	public List<ProductPojo> selectByBrand(Integer brandId){
		return productDao.selectBrand(brandId);
	}
	
	public ProductPojo selectById(Integer id) throws ApiException {
		ProductPojo product = productDao.selectId(id);
		if(product==null)
			throw new ApiException("The product with id "+ id +" does not exists");
		return product;
	}


	public ProductPojo selectByBarcode(String barcode) throws ApiException {
		isEmpty(barcode,barcode);
		ProductPojo product = productDao.selectBarcode(barcode.trim().toLowerCase());
		if(product==null)
			throw new ApiException("The product with barcode "+ barcode +" does not exists");
		return product;
	}


	public void update(ProductPojo product,Integer id) throws ApiException {
		isEmpty(product.getName(),product.getName());
		isNegative(product.getMrp());
		normalize(product);
		ProductPojo prod2 = this.selectById(id);
		prod2.setBrand_category(product.getBrand_category());
		prod2.setMrp(product.getMrp());
		prod2.setName(product.getName());
		productDao.update();
	}

	public void checkMrp(Double sellingPrice, ProductPojo product) throws ApiException {
		if(sellingPrice>product.getMrp())
			throw new ApiException("The MRP of the product is "+product.getMrp()+"!");
	}

	public static void isNegative(Double num) throws ApiException {
		if(num<=0)
			throw new ApiException("Enter a Valid MRP");
	}

	public static void isEmpty(String a, String b) throws ApiException {
		if(a.isEmpty()||b.isEmpty())
			throw new ApiException("Please fill all the fields!");
	}

	protected static void normalize(ProductPojo p) {
		p.setName(p.getName().toLowerCase().trim());
		if(p.getBarcode()!=null)
			p.setBarcode(p.getBarcode().toLowerCase().trim());
	}

}

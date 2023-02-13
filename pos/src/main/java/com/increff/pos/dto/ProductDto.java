package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.increff.pos.model.EditProductForm;
import com.increff.pos.model.StatusReport;
import com.increff.pos.util.ObjectUtil;
import com.increff.pos.util.RefactorUtil;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

import javax.transaction.Transactional;

@Component
public class ProductDto {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;

	@Transactional(rollbackOn = ApiException.class)
	public ProductData createProduct(ProductForm form) throws ApiException {
		//convert form to pojo
		ProductPojo product = ObjectUtil.objectMapper(form, ProductPojo.class);
		//fetch brand by brand name and category
		BrandPojo brand = brandService.selectByNameAndCategory(RefactorUtil.toLowerCase(form.getBrandName()), RefactorUtil.toLowerCase(form.getCategory()));
		//set the brand id to the product pojo
		product.setBrand_category(brand.getId());
		product.setMrp(RefactorUtil.round(product.getMrp(),2));
		//add the product pojo to the database
		return ObjectUtil.objectMapper(productService.add(product), ProductData.class);
	}

	public List<StatusReport> createProducts(List<CSVRecord> records, HashMap<String, Integer> headerMap){
		List<StatusReport> report = new ArrayList<>();
		for(CSVRecord record :records){
			//skip the header
			if(record.getRecordNumber()==1)
				continue;
			ProductForm productForm = new ProductForm();
			productForm.setBrandName(record.get(headerMap.get("brandName")));
			productForm.setCategory(record.get(headerMap.get("category")));
			productForm.setMrp(Double.parseDouble(record.get(headerMap.get("mrp"))));
			productForm.setName(record.get(headerMap.get("name")));
			productForm.setBarcode(record.get(headerMap.get("barcode")));
			try {
				createProduct(productForm);
			}
			catch(ApiException e){
				StatusReport statusReport = new StatusReport();
				statusReport.setBarcode(productForm.getBarcode());
				statusReport.setCategory(productForm.getCategory());
				statusReport.setBrand(productForm.getBrandName());
				statusReport.setName(productForm.getName());
				statusReport.setMrp(productForm.getMrp());
				statusReport.setMessage(e.getMessage());
				report.add(statusReport);
			}
		}
		return report;
	}

	public List<ProductData> getAllProducts() throws ApiException {
		//fetch all products from the database
		List<ProductPojo> products = productService.selectAll();
		List<ProductData> productList = new ArrayList<>();
		//convert pojo to data
		for(ProductPojo product: products) {
			ProductData data = ObjectUtil.objectMapper(product,ProductData.class);
			BrandPojo brand = brandService.selectById(product.getBrand_category());
			data.setBrandName(brand.getBrand());
			data.setCategory(brand.getCategory());
			productList.add(data);
		}
		return productList;
	}
	
	public ProductData getProductsById(Integer id) throws ApiException{
		ProductPojo p = productService.selectById(id);
		return ObjectUtil.objectMapper(p,ProductData.class);
	}
	
	public List<ProductData> getProductByBrandName(String brandName){
		List<BrandPojo> brands = brandService.getByName(brandName);
		System.out.println(brands.get(0).getId());
		List<ProductData> productList = new ArrayList<>();
		for(BrandPojo brand: brands) {
			List<ProductPojo> products = productService.selectByBrand(brand.getId());
			for(ProductPojo product: products) {
				productList.add(ObjectUtil.objectMapper(product,ProductData.class));
			}
		}
		return productList;
	}
	
	public List<ProductData> getProductsByBrandAndCategory(String brandName, String category) throws ApiException {
		BrandPojo brand = brandService.selectByNameAndCategory(brandName, category);
		List<ProductPojo> products = productService.selectByBrand(brand.getId());
		List<ProductData> productList = new ArrayList<>();
		for(ProductPojo product: products) {
			productList.add(ObjectUtil.objectMapper(product,ProductData.class));
		}
		return productList;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateProduct(Integer id, EditProductForm form) throws ApiException {
		//convert form to pojo
		ProductPojo product = ObjectUtil.objectMapper(form, ProductPojo.class);
		//fetch brand by brand name and category
		BrandPojo brand = brandService.selectByNameAndCategory(RefactorUtil.toLowerCase(form.getBrandName()), RefactorUtil.toLowerCase(form.getCategory()));
		product.setBrand_category(brand.getId());
		product.setMrp(RefactorUtil.round(product.getMrp(),2));
		//update the product pojo to the database
		productService.update(product,id);
	}

	//function to get product by barcode
	public ProductData getProductByBarcode(String barcode) throws ApiException {
		ProductPojo p = productService.selectByBarcode(barcode);
		//fetch brand to set brandName and category in ProductData
		BrandPojo brand = brandService.selectById(p.getBrand_category());
		ProductData data = ObjectUtil.objectMapper(p,ProductData.class);
		data.setBrandName(brand.getBrand());
		data.setCategory(brand.getCategory());
		return data;
	}
}

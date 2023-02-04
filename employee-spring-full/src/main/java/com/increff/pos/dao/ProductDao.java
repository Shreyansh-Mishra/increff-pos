package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
	private static final String SELECT_BRAND = "select b from ProductPojo b where b.brand_category in (select c.id from BrandPojo c where c.brand = :brand)";
	private static final String SELECT_BRAND_CATEGORY = "select b from ProductPojo b where brand_category in (select c.id from BrandPojo c where c.brand = :brand and c.category = :category)";
	private static final String SELECT_ALL = "select p from ProductPojo p";
	private static final String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";

	@Transactional
	public void insert(ProductPojo p) {
		em().persist(p);
	}
	
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectBrand(String brand){
		TypedQuery<ProductPojo> query = getQuery(SELECT_BRAND, ProductPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}
	
	public ProductPojo selectBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public List<ProductPojo> selectBrandAndCategory(String brand, String category){
		TypedQuery<ProductPojo> query = getQuery(SELECT_BRAND_CATEGORY, ProductPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public ProductPojo selectId(int id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	
	public void update() {
	}
}

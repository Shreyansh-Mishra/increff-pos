package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_name = "select p from ProductPojo p where name=:name";
	private static String select_brand = "select b from ProductPojo b where b.brand_category in (select c.id from BrandPojo c where c.brand = :brand)";
	private static String select_category = "select b from ProductPojo b where brand_category in (select c.id from BrandPojo c where c.category = :category)";
	private static String select_brand_category = "select b from ProductPojo b where brand_category in (select c.id from BrandPojo c where c.brand = :brand and c.category = :category)";
	private static String select_all = "select p from ProductPojo p";
	private static String delete_id = "delete from ProductPojo b where id=:id";
	
	@Transactional
	public void insert(ProductPojo p) {
		em().persist(p);
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectBrand(String brand){
		TypedQuery<ProductPojo> query = getQuery(select_brand, ProductPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectCategory(String category){
		TypedQuery<ProductPojo> query = getQuery(select_category, ProductPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectBrandAndCategory(String brand, String category){
		TypedQuery<ProductPojo> query = getQuery(select_brand_category, ProductPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public ProductPojo selectName(String name) {
		TypedQuery<ProductPojo> query = getQuery(select_name, ProductPojo.class);
		query.setParameter("name", name);
		return getSingle(query);
	}
	
	public ProductPojo selectId(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public void update() {
	}
}

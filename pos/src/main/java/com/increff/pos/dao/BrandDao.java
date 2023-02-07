package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;
@Repository
public class BrandDao extends AbstractDao {
	private static final String SELECT_ID = "select b from BrandPojo b where id=:id";
	private static final String SELECT_NAME_CATEGORY = "select b from BrandPojo b where brand=:brand and category=:category";
	private static final String SELECT_ALL = "select b from BrandPojo b";
	private static final String SELECT_NAME = "select b from BrandPojo b where brand=:brand";
	private static final String SELECT_CATEGORIES = "select b.category from BrandPojo b where brand=:brand";
	private static final String SELECT_BY_CATEGORY = "select b from BrandPojo b where category=:category";

	@Transactional
	public BrandPojo insert(BrandPojo b) {
		return persist(b);
	}

	public BrandPojo select(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public BrandPojo select(String name, String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_NAME_CATEGORY, BrandPojo.class);
		query.setParameter("brand", name);
		query.setParameter("category", category);
		return getSingle(query);
	}
	
	public List<BrandPojo> select(String name) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_NAME, BrandPojo.class);
		query.setParameter("brand", name);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectAll(){
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}
	
	public List<String> selectCategories(String brand){
		TypedQuery<String> query = getQuery(SELECT_CATEGORIES, String.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectByCategory(String category){
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_CATEGORY, BrandPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public void update(BrandPojo p) {
	}
	
}

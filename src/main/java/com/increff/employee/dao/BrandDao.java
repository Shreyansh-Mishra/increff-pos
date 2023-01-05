package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandPojo;
@Repository
public class BrandDao extends AbstractDao {
	private static String delete_id = "delete from BrandPojo b where id=:id";
	private static String select_id = "select b from BrandPojo b where id=:id";
	private static String select_name_category = "select b from BrandPojo b where brand=:brand and category=:category";
	private static String select_all = "select b from BrandPojo b";
	private static String select_name = "select b from BrandPojo b where brand=:brand";
	
	@Transactional
	public void insert(BrandPojo b) {
		em().persist(b);
	}
	
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public BrandPojo select(String name, String category) {
		TypedQuery<BrandPojo> query = getQuery(select_name_category, BrandPojo.class);
		query.setParameter("brand", name);
		query.setParameter("category", category);
		return getSingle(query);
	}
	
	public List<BrandPojo> select(String name) {
		TypedQuery<BrandPojo> query = getQuery(select_name, BrandPojo.class);
		query.setParameter("brand", name);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectAll(){
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}
	
	
	
	public void update(BrandPojo p) {
	}
	
}

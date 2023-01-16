package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {
	private String select_all = "select i from InventoryPojo i";
	private String select_id = "select i from InventoryPojo i where i.id=:id";
	
	@Transactional
	public void add(InventoryPojo i) {
		em().persist(i);
	}
	
	public InventoryPojo selectId(int id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectAll(){
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}
	
	public List<Object[]> selectInventory() {
		Query query = em().createNativeQuery("select brand, category, b.quantity as quantity from brandpojo "
				+ "inner join "
				+ "(select brand_category, a.quantity as quantity from productpojo "
				+ "inner join "
				+ "(select id,quantity from inventorypojo) as a "
				+ "on "
				+ "a.id = productpojo.id) as b "
				+ "on "
				+ "b.brand_category = brandpojo.id;");
		List<Object[]> o = query.getResultList();
		return o;
	}
	
	public void update(InventoryPojo i) {
	}
	
}

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
	public void insert(InventoryPojo i) {
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

	
	public void update(InventoryPojo i) {
	}
	
}

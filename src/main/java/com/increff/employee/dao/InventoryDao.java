package com.increff.employee.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {
	private String select_all = "select i from InventoryPojo i";
//	private String select_id = "select from InventoryPojo i where i.id=:id";
	
	@Transactional
	public void add(InventoryPojo i) {
		em().merge(i);
	}
	
	public List<InventoryPojo> selectAll(){
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}
	
}

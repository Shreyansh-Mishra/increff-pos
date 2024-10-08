package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {
	private static final String SELECT_ALL = "select i from InventoryPojo i";
	private static final String SELECT_ID = "select i from InventoryPojo i where i.productId=:id";

	private static final String SELECT_INV_ID = "select i from InventoryPojo i where i.id=:id";

	@Transactional
	public InventoryPojo insert(InventoryPojo i) {
		return persist(i);
	}
	
	public InventoryPojo selectId(Integer id) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public InventoryPojo selectByInvId(Integer id) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_INV_ID, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectAll(){
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
	}

	
	public void update(InventoryPojo i) {
	}
	
}

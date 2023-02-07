package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao{
	private static final String SELECT_ID = "select o from OrderItemPojo o where id=:id";
	private static final String SELECT_ITEMS = "select o from OrderItemPojo o where orderId=:id";
	private static final String CHECK_EXISTING = "select o from OrderItemPojo o where productId=:prodId and orderId=:orderId";

	public void insert(OrderItemPojo oi) {
		em().persist(oi);
	}
	
	public OrderItemPojo selectId(Integer id) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderItemPojo> selectItems(Integer id){
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ITEMS, OrderItemPojo.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public OrderItemPojo checkExisting(Integer prodId, Integer orderId){
		TypedQuery<OrderItemPojo> query = getQuery(CHECK_EXISTING, OrderItemPojo.class);
		query.setParameter("prodId", prodId);
		query.setParameter("orderId", orderId);
		return getSingle(query);
	}
	
	public void update(OrderItemPojo o) {
	}
}

package com.increff.employee.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao{
	private String select_id = "select o from OrderItemPojo o where id=:id";
	private String select_items = "select o from OrderItemPojo o where orderId=:id";
	
	public void insert(OrderItemPojo oi) {
		em().persist(oi);
	}
	
	public OrderItemPojo selectId(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderItemPojo> selectItems(int id){
		TypedQuery<OrderItemPojo> query = getQuery(select_items, OrderItemPojo.class);
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	public void update(OrderItemPojo o) {
	}
}

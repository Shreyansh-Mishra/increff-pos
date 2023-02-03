package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao{
	private String select_id = "select o from OrderItemPojo o where id=:id";
	private String select_items = "select o from OrderItemPojo o where orderId=:id";

	private String check_existing = "select o from OrderItemPojo o where productId=:prodId and orderId=:orderId";

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

	public OrderItemPojo checkExisting(int prodId, int orderId){
		TypedQuery<OrderItemPojo> query = getQuery(check_existing, OrderItemPojo.class);
		query.setParameter("prodId", prodId);
		query.setParameter("orderId", orderId);
		return getSingle(query);
	}
	
	public void update(OrderItemPojo o) {
	}
}

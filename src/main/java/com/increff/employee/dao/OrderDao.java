package com.increff.employee.dao;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private String select_id = "select o from OrderItemPojo o where id=:id";
	private String delete_id = "delete from OrderPojo o where id=:id";
	public void insert(OrderItemPojo oi) {
		em().persist(oi);
	}
	public void insertOrder(OrderPojo o) {
		em().persist(o);
	}
	
	public OrderItemPojo selectId(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public void update(OrderItemPojo o) {
	}
}

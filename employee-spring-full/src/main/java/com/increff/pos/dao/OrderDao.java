package com.increff.pos.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private String delete_id = "delete from OrderPojo o where id=:id";
	private String select_all = "select o from OrderPojo o order by o.id DESC";
	private String select_id = "select o from OrderPojo o where id=:id";
	private String select_date = "select o from OrderPojo o where time between :startDate and :endDate";
	private String select_by_date = "select o from OrderPojo o where time between :from and :to";

	public void insert(OrderPojo o) {
		em().persist(o);
	}
	
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public List<OrderPojo> selectAll(){
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}
	
	public OrderPojo selectId(int id){
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectBetweenDates(Instant startDate, Instant endDate){
		TypedQuery<OrderPojo> query = getQuery(select_date, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public List<OrderPojo> selectByDate(Instant from, Instant to){
		TypedQuery<OrderPojo> query = getQuery(select_by_date, OrderPojo.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
	}

}

package com.increff.pos.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private static final String SELECT_ALL = "select o from OrderPojo o order by o.time DESC";
	private static final String SELECT_ID = "select o from OrderPojo o where id=:id";
	private static final String SELECT_DATE = "select o from OrderPojo o where time between :startDate and :endDate";
	private static final String SELECT_BY_DATE = "select o from OrderPojo o where time between :from and :to";

	public OrderPojo insert(OrderPojo o) {
		return persist(o);
	}

	
	public List<OrderPojo> selectAll(){
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
		return query.getResultList();
	}
	
	public OrderPojo selectId(Integer id){
		TypedQuery<OrderPojo> query = getQuery(SELECT_ID, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectBetweenDates(Instant startDate, Instant endDate){
		TypedQuery<OrderPojo> query = getQuery(SELECT_DATE, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public List<OrderPojo> selectByDate(Instant from, Instant to){
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_DATE, OrderPojo.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
	}

}

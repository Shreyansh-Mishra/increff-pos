package com.increff.pos.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.SchedulerPojo;

@Repository
public class SchedulerDao extends AbstractDao{
	private static final String CHECK_DATE = "select s from SchedulerPojo s where date(date)=date(:instant)";
	private static final String SELECT = "select s from SchedulerPojo s";

	public void insert(SchedulerPojo s) {
		em().persist(s);
	}
	
	public SchedulerPojo checkExisting(Instant date) {
		TypedQuery<SchedulerPojo> query = getQuery(CHECK_DATE, SchedulerPojo.class);
		System.out.print(date);
		query.setParameter("instant", date);
		return getSingle(query);
	}
	
	public List<SchedulerPojo> select(){
		TypedQuery<SchedulerPojo> query = getQuery(SELECT, SchedulerPojo.class);
		return query.getResultList();
	}
	
	public void update(SchedulerPojo s) {
	}
}

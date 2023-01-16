package com.increff.employee.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.SchedulerPojo;

@Repository
public class SchedulerDao extends AbstractDao{
	private String checkDate = "select s from SchedulerPojo s where date(date)=date(:instant)";
	private String select = "select s from SchedulerPojo s";
	public void insert(SchedulerPojo s) {
		em().persist(s);
	}
	
	public SchedulerPojo checkExisting(Instant date) {
		TypedQuery<SchedulerPojo> query = getQuery(checkDate, SchedulerPojo.class);
		System.out.print(date);
		query.setParameter("instant", date);
		return getSingle(query);
	}
	
	public List<SchedulerPojo> select(){
		TypedQuery<SchedulerPojo> query = getQuery(select, SchedulerPojo.class);
		return query.getResultList();
	}
	
	public void update(SchedulerPojo s) {
	}
}

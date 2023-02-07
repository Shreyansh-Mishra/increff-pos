package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	private static final String SELECT_ID = "select p from UserPojo p where id=:id";
	private static final String SELECT_EMAIL = "select p from UserPojo p where email=:email";
	private static final String SELECT_ALL = "select p from UserPojo p";

	
	@Transactional
	public void insert(UserPojo p) {
		em().persist(p);
	}


	public UserPojo select(Integer id) {
		TypedQuery<UserPojo> query = getQuery(SELECT_ID, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(SELECT_ALL, UserPojo.class);
		return query.getResultList();
	}

	public void update(UserPojo p) {
	}


}

package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public Boolean add(UserPojo user) throws ApiException {
		normalize(user);
		UserPojo existing = dao.select(user.getEmail());
		if (existing != null) {
			return false;
		}
		dao.insert(user);
		return true;
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo select(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> selectAll() {
		return dao.selectAll();
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}

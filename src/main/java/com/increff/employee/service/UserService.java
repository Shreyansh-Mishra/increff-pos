package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public Boolean add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			return false;
		}
		dao.insert(p);
		return true;
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}

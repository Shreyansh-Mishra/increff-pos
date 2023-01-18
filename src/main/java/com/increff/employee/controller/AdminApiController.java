package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.UserData;
import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Api
@RestController
public class AdminApiController {

	@Autowired
	private UserService service;

	@Autowired
	InfoData info;

	@Value("${signup.email}")
	private String email;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/admin/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView addUser(HttpServletRequest req, UserForm form) throws ApiException {
		UserPojo p = convert(form);
		if(p.getEmail().equals(email)){ p.setRole("supervisor"); }
		else{ p.setRole("operator"); }
		Boolean isSuccess = service.add(p);
		if(isSuccess){
			info.setMessage("Signup Successful!");
			return new ModelAndView("redirect:/site/login");
		}
		else{
			info.setMessage("The email is already registered!");
		}
		return new ModelAndView("redirect:/site/signup");
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/admin/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/admin/user", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	
	
	private static UserData convert(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	private static UserPojo convert(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setPassword(f.getPassword());
		return p;
	}

}

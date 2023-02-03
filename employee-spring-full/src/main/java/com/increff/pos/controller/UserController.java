package com.increff.pos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.LoginForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/session")
public class UserController {

	@Autowired
	private UserDto userDto;
	
	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		UserPojo user = userDto.login(f);
		if(user==null){
			return new ModelAndView("redirect:/site/login");
		}
		// Create authentication object
		Authentication authentication = convert(user);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);
		return new ModelAndView("redirect:/ui/home");
	}

	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		return userDto.logout(request);
	}

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView addUser(HttpServletRequest req, UserForm form) throws ApiException {
	    return userDto.signup(form);
	}

	private static Authentication convert(UserPojo p) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(p.getEmail());
		principal.setId(p.getId());
		principal.setRole(p.getRole());

		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(p.getRole()));
		// you can add more roles if required

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}


}

package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/employee")
	public ModelAndView brands() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView products() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/ui/orders")
	public ModelAndView orders() {
		return mav("order.html");
	}

}

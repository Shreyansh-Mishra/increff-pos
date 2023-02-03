package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui")
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/brands")
	public ModelAndView brands() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/products")
	public ModelAndView products() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/orders")
	public ModelAndView orders() {
		return mav("order.html");
	}
	
	@RequestMapping(value = "/report/day-report")
	public ModelAndView dayReport() {
		return mav("DayWiseReport.html");
	}
	
	@RequestMapping(value = "/report/sales-report")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}
	
	@RequestMapping(value = "/report/inventory-report")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}


}

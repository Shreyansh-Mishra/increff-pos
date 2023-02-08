package com.increff.pos.controller;

import java.text.ParseException;
import java.util.List;

import com.increff.pos.model.BrandReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.DayWiseReportData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesByBrandAndCategoryData;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api")
public class ReportController {
	@Autowired
    ReportDto salesReportDto;
	
	
	@ApiOperation(value="Scheduler")
	@RequestMapping(path="/report/day-wise-report", method=RequestMethod.GET)
	public List<DayWiseReportData> getDayByDayReport(@RequestParam String startDate, @RequestParam String endDate) throws ParseException, ApiException {
		return salesReportDto.getDayWiseReport(startDate, endDate);
	}
	
	@ApiOperation(value="Get Sales Report")
	@RequestMapping(path="/report/{brandName}/{category}/{startDate}/{endDate}/sales-report",method=RequestMethod.GET)
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(@PathVariable String brandName, @PathVariable String category, @PathVariable String startDate, @PathVariable String endDate) throws ParseException, ApiException{
		System.out.println("brandName: "+brandName);
		return salesReportDto.getSalesByBrandAndCategory(startDate, endDate, brandName, category);
	}
	
	@ApiOperation(value="Get Inventory Report")
	@RequestMapping(path="/report/inventory-report/{brand}/{category}",method=RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport(@PathVariable String brand, @PathVariable String category) throws ApiException {
		return salesReportDto.getInventoryReport(brand,category);
	}

	@ApiOperation(value="Get Brand Report")
	@RequestMapping(path="/report/{brand}/{category}/brand-report", method=RequestMethod.GET)
	public List<BrandReportData> getBrandReport(@PathVariable String brand, @PathVariable String category) throws ApiException{
		return salesReportDto.getBrandReport(brand, category);
	}
}

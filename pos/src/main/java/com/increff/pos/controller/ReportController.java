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
	@RequestMapping(path="/report/sales-report",method=RequestMethod.GET)
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(@RequestParam String brandName, @RequestParam String category, @RequestParam String startDate, @RequestParam String endDate) throws ParseException, ApiException{
		return salesReportDto.getSalesByBrandAndCategory(startDate, endDate, brandName, category);
	}
	
	@ApiOperation(value="Get Inventory Report")
	@RequestMapping(path="/report/inventory-report",method=RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport(@RequestParam String brand, @RequestParam String category) throws ApiException {
		return salesReportDto.getInventoryReport(brand,category);
	}

	@ApiOperation(value="Get Brand Report")
	@RequestMapping(path="/report/brand-report", method=RequestMethod.GET)
	public List<BrandReportData> getBrandReport(@RequestParam String brand, @RequestParam String category) throws ApiException{
		return salesReportDto.getBrandReport(brand, category);
	}
}

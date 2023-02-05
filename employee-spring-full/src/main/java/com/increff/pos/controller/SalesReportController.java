package com.increff.pos.controller;

import java.text.ParseException;
import java.util.List;

import com.increff.pos.model.BrandReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class SalesReportController {
	@Autowired
    ReportDto salesReportDto;
	
	
	@ApiOperation(value="Scheduler")
	@RequestMapping(path="/report/day-wise-report/{startDate}/{endDate}", method=RequestMethod.GET)
	public List<DayWiseReportData> getDayByDayReport(@PathVariable String startDate, @PathVariable String endDate) throws ParseException{
		return salesReportDto.getDayWiseReport(startDate, endDate);
	}
	
	@ApiOperation(value="Get Sales Report")
	@RequestMapping(path="/report/{brandName}/{category}/{startDate}/{endDate}/sales-report",method=RequestMethod.GET)
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(@PathVariable String brandName, @PathVariable String category, @PathVariable String startDate, @PathVariable String endDate) throws ParseException, ApiException{
		System.out.println("brandName: "+brandName);
		return salesReportDto.getSalesByBrandAndCategory(startDate, endDate, brandName, category);
	}
	
	@ApiOperation(value="Get Inventory Report")
	@RequestMapping(path="/report/inventory-report",method=RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		return salesReportDto.getInventoryReport();
	}

	@ApiOperation(value="Get Brand Report")
	@RequestMapping(path="/report/{brand}/{category}/get-brand-report", method=RequestMethod.GET)
	public List<BrandReportData> getBrandReport(@PathVariable String brand, @PathVariable String category) throws ApiException{
		return salesReportDto.getBrandReport(brand, category);
	}
}

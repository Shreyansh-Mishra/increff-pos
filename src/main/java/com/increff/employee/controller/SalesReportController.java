package com.increff.employee.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.flow.ReportFlow;
import com.increff.employee.model.DayWiseReportData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesByBrandAndCategoryData;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class SalesReportController {
	@Autowired
    ReportFlow salesReportFlow;
	
	
	@ApiOperation(value="Scheduler")
	@RequestMapping(path="/api/report/get-sales-report", method=RequestMethod.GET)
	public List<DayWiseReportData> getDayByDayReport() throws ParseException{
		return salesReportFlow.getDayWiseReport();
	}
	
	@ApiOperation(value="Get Sales Report")
	@RequestMapping(path="/api/report/get-sales-report/{startDate}/{endDate}",method=RequestMethod.GET)
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(@PathVariable String startDate, @PathVariable String endDate) throws ParseException, ApiException{
		return salesReportFlow.getSalesByBrandAndCategory(startDate, endDate);
	}
	
	@ApiOperation(value="Get Inventory Report")
	@RequestMapping(path="/api/report/get-inventory-report",method=RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		return salesReportFlow.getInventoryReport();
	}
}

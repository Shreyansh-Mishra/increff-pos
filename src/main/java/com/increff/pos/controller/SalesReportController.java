package com.increff.pos.controller;

import java.text.ParseException;
import java.util.List;

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
public class SalesReportController {
	@Autowired
    ReportDto salesReportDto;
	
	
	@ApiOperation(value="Scheduler")
	@RequestMapping(path="/api/report/get-sales-report", method=RequestMethod.GET)
	public List<DayWiseReportData> getDayByDayReport() throws ParseException{
		return salesReportDto.getDayWiseReport();
	}
	
	@ApiOperation(value="Get Sales Report")
	@RequestMapping(path="/api/report/get-sales-report/{startDate}/{endDate}",method=RequestMethod.GET)
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(@PathVariable String startDate, @PathVariable String endDate) throws ParseException, ApiException{
		return salesReportDto.getSalesByBrandAndCategory(startDate, endDate);
	}
	
	@ApiOperation(value="Get Inventory Report")
	@RequestMapping(path="/api/report/get-inventory-report",method=RequestMethod.GET)
	public List<InventoryReportData> getInventoryReport() throws ApiException {
		return salesReportDto.getInventoryReport();
	}
}

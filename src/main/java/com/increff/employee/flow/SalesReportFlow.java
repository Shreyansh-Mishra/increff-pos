package com.increff.employee.flow;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.DayWiseReportData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesByBrandAndCategoryData;
import com.increff.employee.pojo.SchedulerPojo;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderService;

@Component
public class SalesReportFlow {
	@Autowired
	OrderService orderService;
	
	@Autowired
	InventoryService inventoryService;
	
	
	public List<DayWiseReportData> getDayWiseReport() {
		return convert(orderService.getSchedulerData());
	}
	
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(String startDate, String endDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
        Instant from = date.toInstant();
		Instant to = date2.toInstant();
		List<Object[]> orders = orderService.getReportByBrandAndCategory(from, to);
		List<SalesByBrandAndCategoryData> data = new ArrayList<SalesByBrandAndCategoryData>();
		for(Object[] order: orders) {
			SalesByBrandAndCategoryData sale = convertData(order);
			data.add(sale);
		}
		return data;
	}
	
	public List<InventoryReportData> getInventoryReport(){
		List<Object[]> rows = inventoryService.getInventoryReport();
		List<InventoryReportData> data = new ArrayList<InventoryReportData>();
		for(Object[] row: rows) {
			InventoryReportData item = convertToInventoryData(row);
			data.add(item);
		}
		return data;
	}
	
	
	public static InventoryReportData convertToInventoryData(Object[] row) {
		InventoryReportData data = new InventoryReportData();
		data.setBrand(row[0].toString());
		data.setCategory(row[1].toString());
		data.setQuantity((int)row[2]);
		return data;
	}
	
	public static List<DayWiseReportData> convert(List<SchedulerPojo> days) {
		List<DayWiseReportData> data = new ArrayList<DayWiseReportData>();
		for(SchedulerPojo day: days) {
			DayWiseReportData d = new DayWiseReportData();
			d.setDate(day.getDate().toString());
			d.setInvoiced_items_count(day.getInvoiced_items_count());
			d.setInvoiced_orders_count(day.getInvoiced_orders_count());
			d.setTotal_revenue(day.getRevenue());
			data.add(d);
		}
		return data;
	}
	
	public static SalesByBrandAndCategoryData convertData(Object[] sale) {
		SalesByBrandAndCategoryData data = new SalesByBrandAndCategoryData();
		data.setBrand(sale[0].toString());
		data.setCategory(sale[1].toString());
		data.setQuantity(((BigInteger)sale[2]).intValue());
		data.setRevenue(((double)sale[3]));
		return data;
	}
	
	
}

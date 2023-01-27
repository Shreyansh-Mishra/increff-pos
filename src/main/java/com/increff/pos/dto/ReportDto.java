package com.increff.pos.dto;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.DayWiseReportData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesByBrandAndCategoryData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.SchedulerPojo;

@Component
public class ReportDto {
	@Autowired
	OrderService orderService;
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	ProductService productService;

	@Autowired
	OrderItemsService orderItemsService;

	@Autowired
	private SchedulerService schedulerService;

	//schedule at 12:05 am everyday

	public List<DayWiseReportData> getDayWiseReport() {
		return convert(schedulerService.selectSchedulerData());
	}
	
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(String startDate, String endDate) throws ParseException, ApiException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
        Instant from = date.toInstant();
		Instant to = date2.toInstant();
		
		//map of brand id and list<product id>
		HashMap<Integer,List<Integer>> map=new HashMap<Integer,List<Integer>>();
		
		//map of product id and revenue
		HashMap<Integer,List<Double>> map2 = new HashMap<Integer, List<Double>>();
		
		List<BrandPojo> brands = brandService.selectAll();
		List<ProductPojo> products = productService.selectAll();
		
		//loop for storing all product ids with respect to brand and category
		for(BrandPojo brand: brands) {
			List<Integer> productids = new ArrayList<Integer>();
			for(ProductPojo product: products) {
				if(product.getBrand_category()==brand.getId()) {
					productids.add(product.getId());
				}
			}
			map.put(brand.getId(), productids);
		}
		
		List<OrderPojo> orders = orderService.selectOrdersBetweenDates(from, to);
		for(OrderPojo order: orders) {
			List<OrderItemPojo> orderItems = orderItemsService.selectItems(order.getId());
			double revenue = 0;
			for(OrderItemPojo orderItem: orderItems) {
				revenue=(orderItem.getSellingPrice()*orderItem.getQuantity());
				if(map2.containsKey(orderItem.getProductId())) {
					List<Double> pr= map2.get(orderItem.getProductId());
					List<Double> newpr = new ArrayList<Double>();
					newpr.add(pr.get(0)+revenue);
					newpr.add(pr.get(1)+orderItem.getQuantity());
					map2.put(orderItem.getProductId(), newpr);
				}
				else {
					List<Double> newpr = new ArrayList<Double>();
					newpr.add(revenue);
					newpr.add((double)orderItem.getQuantity());
					map2.put(orderItem.getProductId(), newpr);
				}
			}
		}
		System.out.println("here");
		
		//map of brand id and revenue
		HashMap<Integer,List<Double>> map3 = new HashMap<Integer, List<Double>>();
		for(Integer key: map.keySet()) {
			List<Integer> prod = map.get(key);
			for(int p: prod) {
				if(map2.containsKey(p)) {
					double newRevenue=0;
					double newQuantity=0;
					if(map3.containsKey(key)) {
						newRevenue = map3.get(key).get(0)+map2.get(p).get(0);
						newQuantity = map3.get(key).get(1)+map2.get(p).get(1);
					}
					else {
						newRevenue = map2.get(p).get(0);
						newQuantity = map2.get(p).get(1);
					}
					List<Double> newData = new ArrayList<Double>();
					newData.add(newRevenue);
					newData.add(newQuantity);
					map3.put(key, newData);
				}
			}
		}
		List<SalesByBrandAndCategoryData> data = convert(map3);
		return data;
	}
	
	
	public List<InventoryReportData> getInventoryReport() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<InventoryReportData> inventoryReport = new ArrayList<InventoryReportData>();
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(InventoryPojo item: inventory) {
			ProductPojo product = productService.selectById(item.getId());
			if(map.containsKey(product.getBrand_category())) {
				map.put(product.getBrand_category(), map.get(product.getBrand_category())+item.getQuantity());
			}
			else {
				map.put(product.getBrand_category(), item.getQuantity());
			}
		}
		for(int key: map.keySet()) {
			BrandPojo brand = brandService.selectById(key);
			InventoryReportData data = new InventoryReportData();
			data.setQuantity(map.get(key));
			data.setBrand(brand.getBrand());
			data.setCategory(brand.getCategory());
			inventoryReport.add(data);
		}
		return inventoryReport;
	}
	
	public List<SalesByBrandAndCategoryData> convert(HashMap<Integer,List<Double>> m) throws ApiException{
		List<SalesByBrandAndCategoryData> data = new ArrayList<SalesByBrandAndCategoryData>();
		System.out.println("here3");
		for(int key: m.keySet()) {
			SalesByBrandAndCategoryData d = new SalesByBrandAndCategoryData();
			BrandPojo brand = brandService.selectById(key);
			d.setRevenue(m.get(key).get(0));
			d.setBrand(brand.getBrand());
			d.setCategory(brand.getCategory());
			d.setQuantity((m.get(key).get(1)).intValue());
			data.add(d);
		}
		System.out.println("here4");
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

package com.increff.pos.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.increff.pos.model.BrandReportData;
import com.increff.pos.service.*;
import com.increff.pos.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

import javax.transaction.Transactional;

import com.increff.pos.util.StringUtil;

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

	//schedule at 12:01 am everyday

	//set cron timezone to utc
	@Scheduled(cron = "0 0 0 * * *" , zone = "UTC")
	@Transactional
	public void updateScheduler(){
		SchedulerPojo scheduler = new SchedulerPojo();
		Instant from = Instant.now().minusSeconds(600).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
		Instant to = Instant.now();
		List<OrderPojo> o = orderService.selectByDate(from,to);
		System.out.println(o.size());
		Double revenue = 0.0;
		Integer itemcount = 0;
		for(OrderPojo order: o){
			List<OrderItemPojo> items = orderItemsService.selectItems(order.getId());
			for(OrderItemPojo item: items){
				revenue += item.getMrp()*item.getQuantity();
				itemcount+=item.getQuantity();
			}
		}
		scheduler.setRevenue(StringUtil.round(revenue, 2));
		scheduler.setInvoiced_items_count(itemcount);
		scheduler.setInvoiced_orders_count(o.size());
		scheduler.setDate(from);
		schedulerService.insertScheduler(scheduler);
	}

	public List<DayWiseReportData> getDayWiseReport(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("utc"));
		Date date = sdf.parse(startDate);
		Date date2 = sdf.parse(endDate);
		Instant from = date.toInstant();
		Instant to = date2.toInstant().plus(1, ChronoUnit.DAYS);
		return convert(schedulerService.selectSchedulerData(from, to));
	}
	
	public List<SalesByBrandAndCategoryData> getSalesByBrandAndCategory(String startDate, String endDate, String brandName, String category) throws ParseException, ApiException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("utc"));
		sdf.setLenient(false);
        System.out.println(endDate);
		Date date = sdf.parse(startDate);
        Date date2 = sdf.parse(endDate);
		System.out.println("date2: "+date2);
        Instant from = date.toInstant();
		Instant to = date2.toInstant();
		
		//map of brand id and list<product id>
		HashMap<Integer,List<Integer>> map=new HashMap<>();
		
		//map of product id and revenue
		HashMap<Integer,List<Double>> map2 = new HashMap<>();
		List<BrandPojo> brands = new ArrayList<>();
		if(brandName.equals("all") && category.equals("all")){
			System.out.println("all");
			brands = brandService.selectAll();
		}
		else if(brandName.equals("all")){
			System.out.println("all 2");
			brands = brandService.getByCategory(category);
		}
		else if(category.equals("all")){
			System.out.println("all 3");
			brands = brandService.getByName(brandName);
		}
		else{
			System.out.println("all 4");
			brands.add(brandService.selectByNameAndCategory(brandName, category));
		}
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
		System.out.println("Order:"+orders.size()+"from: "+ from +"to: "+ to);
		for(OrderPojo order: orders) {
			List<OrderItemPojo> orderItems = orderItemsService.selectItems(order.getId());
			Double revenue = 0.0;
			for(OrderItemPojo orderItem: orderItems) {
				revenue=(orderItem.getMrp()*orderItem.getQuantity());
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
					newpr.add(orderItem.getQuantity().doubleValue());
					map2.put(orderItem.getProductId(), newpr);
				}
			}
		}
		System.out.println("here");
		
		//map of brand id and revenue
		HashMap<Integer,List<Double>> map3 = new HashMap<Integer, List<Double>>();
		for(Integer key: map.keySet()) {
			List<Integer> prod = map.get(key);
			for(Integer p: prod) {
				if(map2.containsKey(p)) {
					Double newRevenue=0.0;
					Double newQuantity=0.0;
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
		for(Integer key: map.keySet()) {
			BrandPojo brand = brandService.selectById(key);
			InventoryReportData data = new InventoryReportData();
			data.setQuantity(map.get(key));
			data.setBrand(brand.getBrand());
			data.setCategory(brand.getCategory());
			inventoryReport.add(data);
		}
		return inventoryReport;
	}

	public List<BrandReportData> getBrandReport(String brand, String category) throws ApiException{
		brand=brand.toLowerCase();
		category=category.toLowerCase();
		List<BrandReportData> data = new ArrayList<>();
		if(brand.equals("all")&& category.equals("all")){
			List<BrandPojo> brands = brandService.selectAll();
			for(BrandPojo b:  brands){
				data.add(ObjectUtil.objectMapper(b, BrandReportData.class));
			}
		}
		else if(category.equals("all")){
			List<BrandPojo> brands = brandService.getByName(brand);
			for(BrandPojo b: brands){
				data.add(ObjectUtil.objectMapper(b, BrandReportData.class));
			}
		}
		else if(brand.equals("all")){
			List<BrandPojo> brands = brandService.getByCategory(category);
			for(BrandPojo b: brands){
				data.add(ObjectUtil.objectMapper(b, BrandReportData.class));
			}
		}
		else{
			BrandPojo b = brandService.selectByNameAndCategory(brand, category);
			data.add(ObjectUtil.objectMapper(b, BrandReportData.class));
		}
		return data;
	}
	
	public List<SalesByBrandAndCategoryData> convert(HashMap<Integer,List<Double>> m) throws ApiException{
		List<SalesByBrandAndCategoryData> data = new ArrayList<SalesByBrandAndCategoryData>();
		for(Integer key: m.keySet()) {
			SalesByBrandAndCategoryData d = new SalesByBrandAndCategoryData();
			BrandPojo brand = brandService.selectById(key);
			d.setRevenue(StringUtil.round(m.get(key).get(0), 2));
			d.setBrand(brand.getBrand());
			d.setCategory(brand.getCategory());
			d.setQuantity((m.get(key).get(1)).intValue());
			data.add(d);
		}
		return data;
	}
	

	public static List<DayWiseReportData> convert(List<SchedulerPojo> days) {
		List<DayWiseReportData> data = new ArrayList<DayWiseReportData>();
		for(SchedulerPojo day: days) {
			DayWiseReportData d = new DayWiseReportData();
			d.setDate(day.getDate().toString());
			d.setInvoiced_items_count(day.getInvoiced_items_count());
			d.setInvoiced_orders_count(day.getInvoiced_orders_count());
			d.setTotal_revenue(StringUtil.round(day.getRevenue(), 2));
			data.add(d);
		}
		return data;
	}
}

package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.example.dto.InvoiceDto;
import org.example.model.OrderFOPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemData;

import javax.transaction.Transactional;

@Component
public class OrderDto {

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;


	@Autowired
	private OrderItemsService orderItemsService;

	@Autowired
	private InvoiceDto invoiceDto;

	@Transactional(rollbackOn = ApiException.class)
	public void createOrder(List<OrderForm> o) throws ApiException, Exception {
		List<OrderItemPojo> o2 = convert(o);
		for(OrderItemPojo item: o2){
			ProductPojo p = productService.selectByBarcode(item.getBarcode());
			InventoryPojo i = new InventoryPojo();
			InventoryPojo i2 = inventoryService.selectById(p.getId());
			i.setBarcode(p.getBarcode());
			int newQuantity = i2.getQuantity()-item.getQuantity();
			i.setQuantity(newQuantity);
			i.setId(p.getId());
			i.setBarcode(p.getBarcode());
			inventoryService.update(i);
			item.setProductId(p.getId());
		}
		OrderPojo order = new OrderPojo();
		orderService.addOrder(order);
		orderItemsService.addItems(o2,order.getId());
		String pdf = generateInvoice(order.getId());
		//decode the pdf from the base64 string
		byte[] decodedBytes = java.util.Base64.getDecoder().decode(pdf);
		String date = order.getTime().toString();
		date = date.split("T", 2)[0] + "-" + date.split("T", 2)[1].split(":")[0] + "-" + date.split("T", 2)[1].split(":")[1];
		String fileName = "Invoice-"+order.getId()+"-"+date+".pdf";
		//write the decoded bytes to a file in the resources folder
		java.nio.file.Path path = java.nio.file.Paths.get("C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName);
		java.nio.file.Files.write(path, decodedBytes);
		String path2 = "C:\\Users\\Shreyansh\\Desktop\\increff\\employee-spring-full\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName;
		InvoicePojo invoice = convert(order.getId(), path2);
		orderService.insertInvoice(invoice);
	}

	public String generateInvoice(int orderId) throws Exception {
		OrderPojo order = orderService.selectOrderById(orderId);
		List<OrderItemPojo> items = orderItemsService.selectItems(orderId);
		OrderFOPObject orderFop = new OrderFOPObject();
		orderFop.setOrderId(order.getId());
		orderFop.setDate(order.getTime().toString().split("T")[0]);
		List<org.example.model.OrderItemData> fopItems = new ArrayList<org.example.model.OrderItemData>();
		double total = 0;
		for(OrderItemPojo item: items) {
			org.example.model.OrderItemData i = new org.example.model.OrderItemData();
			ProductPojo product = productService.selectById(item.getProductId());
			i.setItemName(product.getName());
			i.setBarcode(product.getBarcode());
			i.setOrderId(item.getOrderId());
			i.setQuantity(item.getQuantity());
			i.setSellingPrice(item.getSellingPrice());
			i.setCost(item.getSellingPrice()*item.getQuantity());
			total += i.getCost();
			fopItems.add(i);
		}
		orderFop.setOrderItems(fopItems);
		orderFop.setTotal(total);
		return invoiceDto.generateInvoice(orderFop);
	}
	
	public List<OrderData> getOrders(){
		List<OrderPojo> orders = orderService.selectOrders();
		List<OrderData> orderData = new ArrayList<OrderData>();
		for(OrderPojo order: orders) {
			orderData.add(convert(order));
		}
		return orderData;
	}
	
	public List<OrderItemData> getOrderItems(int id) throws ApiException{
		List<OrderItemPojo> items = orderItemsService.selectItems(id);
		List<OrderItemData> orderItems = new ArrayList<OrderItemData>();
		for(OrderItemPojo i: items) {
			OrderItemData o = convert(i);
			orderItems.add(o);
		}
		return orderItems;
	}

	public InvoicePojo getInvoice(int id) throws ApiException{
		InvoicePojo invoice = orderService.selectInvoice(id);
		return invoice;
	}
	
	public static OrderData convert(OrderPojo order) {
		OrderData data = new OrderData();
		String time = order.getTime().toString();
		data.setId(order.getId());
		data.setTime(time);
		return data;
	}

	public static InvoicePojo convert(int id, String path){
		InvoicePojo invoice = new InvoicePojo();
		invoice.setId(id);
		invoice.setPath(path);
		return invoice;
	}
	
	public OrderItemData convert(OrderItemPojo o) throws ApiException {
		OrderItemData data = new OrderItemData();
		ProductPojo p = productService.selectById(o.getProductId());
		data.setBarcode(p.getBarcode());
		data.setItemName(p.getName());
		data.setOrderId(o.getOrderId());
		data.setQuantity(o.getQuantity());
		data.setSellingPrice(o.getSellingPrice());
		return data;
	}
	
	public static OrderItemPojo convert(OrderForm o) {
		OrderItemPojo o2 = new OrderItemPojo();
		o2.setQuantity(o.getQuantity());
		o2.setSellingPrice(o.getMrp());
		o2.setBarcode(o.getBarcode());
		return o2;
	}
	
	public static List<OrderItemPojo> convert(List<OrderForm> form){
		List<OrderItemPojo> list= new ArrayList<OrderItemPojo>();
		for(OrderForm i: form) {
			OrderItemPojo o = convert(i);
			list.add(o);
		}
		return list;
	}
}

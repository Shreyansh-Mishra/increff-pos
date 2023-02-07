package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ObjectUtil;
import com.increff.pos.util.StringUtil;
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
	@Autowired
	private InvoiceService invoiceService;

	@Transactional(rollbackOn = ApiException.class)
	public void createOrder(List<OrderForm> o) throws Exception {
		//create an order
		List<OrderItemPojo> orderItems = convert(o);
		updateOrderInventory(orderItems);
		OrderPojo order = new OrderPojo();
		orderService.addOrder(order);
		orderItemsService.addItems(orderItems,order.getId());

		//generate the invoice and get the base64 string
		String pdf = generateInvoice(order.getId());

		//decode the pdf from the base64 string
		byte[] decodedPdf = java.util.Base64.getDecoder().decode(pdf);

		String date = order.getTime().toString();
		date = date.split("T", 2)[0] + "-" + date.split("T", 2)[1].split(":")[0] + "-" + date.split("T", 2)[1].split(":")[1];
		String fileName = "Invoice-"+order.getId()+"-"+date+".pdf";

		//write the decoded bytes to a file in the resources folder
		java.nio.file.Path path = java.nio.file.Paths.get("C:\\Users\\Shreyansh\\Desktop\\increff2\\point-of-sale\\pos\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName);
		java.nio.file.Files.write(path, decodedPdf);

		//insert the invoice path in the database
		String path2 = "C:\\Users\\Shreyansh\\Desktop\\increff2\\point-of-sale\\pos\\src\\main\\resources\\com\\increff\\pos\\invoices\\"+fileName;
		InvoicePojo invoice = convert(order.getId(), path2);
		invoiceService.insertInvoice(invoice);
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateOrderInventory(List<OrderItemPojo> orderItems) throws ApiException {
		for(OrderItemPojo item: orderItems){
			ProductPojo product = productService.selectByBarcode(item.getBarcode());
			productService.checkMrp(item.getMrp(), product);
			InventoryPojo newInventory = new InventoryPojo();
			InventoryPojo oldInventory = inventoryService.selectById(product.getId());
			newInventory.setBarcode(product.getBarcode());
			newInventory.setQuantity(oldInventory.getQuantity()-item.getQuantity());
			newInventory.setId(product.getId());
			newInventory.setBarcode(product.getBarcode());
			inventoryService.update(newInventory);
			item.setProductId(product.getId());
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public String generateInvoice(Integer orderId) throws Exception {
		OrderPojo order = orderService.selectOrderById(orderId);
		List<OrderItemPojo> orderItems = orderItemsService.selectItems(orderId);

		//fop object of the invoice module
		OrderFOPObject orderFop = new OrderFOPObject();
		orderFop.setOrderId(order.getId());
		orderFop.setDate(order.getTime().toString().split("T")[0]);
		List<org.example.model.OrderItemData> fopItems = new ArrayList<>();
		Double total = 0.0;
		for(OrderItemPojo item: orderItems) {
			org.example.model.OrderItemData itemData = new org.example.model.OrderItemData();
			ProductPojo product = productService.selectById(item.getProductId());
			itemData.setItemName(product.getName());
			itemData.setBarcode(product.getBarcode());
			itemData.setOrderId(item.getOrderId());
			itemData.setQuantity(item.getQuantity());
			itemData.setSellingPrice(StringUtil.round(item.getMrp(),2));
			itemData.setCost(item.getMrp()*item.getQuantity());
			total += itemData.getCost();
			fopItems.add(itemData);
		}
		orderFop.setOrderItems(fopItems);
		orderFop.setTotal(StringUtil.round(total,2));
		return invoiceDto.generateInvoice(orderFop);
	}
	
	public List<OrderData> getOrders(){
		List<OrderPojo> orders = orderService.selectOrders();
		List<OrderData> orderData = new ArrayList<>();
		for(OrderPojo order: orders) {
			orderData.add(convert(order));
		}
		return orderData;
	}
	
	public List<OrderItemData> getOrderItems(Integer id) throws ApiException {
		List<OrderItemPojo> items = orderItemsService.selectItems(id);
		List<OrderItemData> orderItemsData = new ArrayList<>();
		for(OrderItemPojo item: items) {
			System.out.println(item.getMrp());
			OrderItemData o = ObjectUtil.objectMapper(item,OrderItemData.class);
			System.out.println(o.getMrp());
			ProductPojo p = productService.selectById(item.getProductId());
			o.setItemName(p.getName());
			o.setBarcode(p.getBarcode());
			orderItemsData.add(o);
		}
		return orderItemsData;
	}

	public InvoicePojo getInvoice(Integer id) throws ApiException{
		return invoiceService.selectInvoice(id);
	}



	public static InvoicePojo convert(Integer id, String path){
		InvoicePojo invoice = new InvoicePojo();
		invoice.setId(id);
		invoice.setPath(path);
		return invoice;
	}

	public static OrderData convert(OrderPojo order) {
		OrderData data = new OrderData();
		String time = order.getTime().toString();
		data.setId(order.getId());
		data.setTime(time);
		return data;
	}

	public static List<OrderItemPojo> convert(List<OrderForm> form){
		List<OrderItemPojo> list= new ArrayList<>();
		for(OrderForm i: form) {
			OrderItemPojo o = ObjectUtil.objectMapper(i,OrderItemPojo.class);
			o.setMrp(StringUtil.round(i.getMrp(), 2));
			list.add(o);
		}
		return list;
	}
}

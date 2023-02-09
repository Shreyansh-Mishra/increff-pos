package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.ObjectUtil;
import com.increff.pos.util.RefactorUtil;
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
	public OrderData createOrder(List<OrderForm> o) throws Exception {
		//everything is wrapped in a transaction so if any error occurs, the database is rolled back to the previous state
		//create an order
		List<OrderItemPojo> orderItems = ObjectUtil.convert(o);
		List<String> barcodes = new ArrayList<>();
		for(OrderForm orderForm : o){
			barcodes.add(orderForm.getBarcode());
		}
		updateOrderInventory(orderItems,barcodes);
		OrderPojo order = new OrderPojo();
		OrderPojo orderPojo = orderService.addOrder(order);
		OrderData orderData = ObjectUtil.convert(orderPojo);
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
		InvoicePojo invoice = ObjectUtil.convert(order.getId(), path2);
		invoiceService.insertInvoice(invoice);
		return orderData;
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updateOrderInventory(List<OrderItemPojo> orderItems,List<String> barcodes) throws ApiException {
		int i = 0;
		for(OrderItemPojo item: orderItems){
			//select the product by barcode
			ProductPojo product = productService.selectByBarcode(barcodes.get(i));
			//check if mrp is less than selling price
			productService.checkMrp(item.getMrp(), product);
			//update the inventory of the product
			InventoryPojo newInventory = new InventoryPojo();
			InventoryPojo oldInventory = inventoryService.selectById(product.getId());
			newInventory.setQuantity(oldInventory.getQuantity()-item.getQuantity());
			newInventory.setId(product.getId());
			inventoryService.update(newInventory, barcodes.get(i++));
			item.setProductId(product.getId());
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public String generateInvoice(Integer orderId) throws Exception {
		OrderPojo order = orderService.selectOrderById(orderId);
		List<OrderItemPojo> orderItems = orderItemsService.selectItems(orderId);
		//plan is to fetch the models from invoice module, set them here and then pass them to the invoice module
		//fop object of the invoice module
		OrderFOPObject orderFop = new OrderFOPObject();
		orderFop.setOrderId(order.getId());
		orderFop.setDate(order.getTime().toString().split("T")[0]);
		//OrderItemData object is from the invoice module
		List<org.example.model.OrderItemData> fopItems = new ArrayList<>();
		double total = 0.0;
		for(OrderItemPojo item: orderItems) {
			org.example.model.OrderItemData itemData = new org.example.model.OrderItemData();
			ProductPojo product = productService.selectById(item.getProductId());
			itemData.setItemName(product.getName());
			itemData.setBarcode(product.getBarcode());
			itemData.setOrderId(item.getOrderId());
			itemData.setQuantity(item.getQuantity());
			itemData.setSellingPrice(RefactorUtil.round(item.getMrp(),2));
			itemData.setCost(item.getMrp()*item.getQuantity());
			total += itemData.getCost();
			fopItems.add(itemData);
		}
		//set the attributes of the fop object
		orderFop.setOrderItems(fopItems);
		orderFop.setTotal(RefactorUtil.round(total,2));
		//call the generateInvoice method of the invoice module
		return invoiceDto.generateInvoice(orderFop);
	}
	
	public List<OrderData> getOrders(){
		//select all the orders
		List<OrderPojo> orders = orderService.selectOrders();
		List<OrderData> orderData = new ArrayList<>();
		//convert the pojo to data
		for(OrderPojo order: orders) {
			orderData.add(ObjectUtil.convert(order));
		}
		return orderData;
	}
	
	public List<OrderItemData> getOrderItems(Integer id) throws ApiException {
		//select all the order items
		List<OrderItemPojo> items = orderItemsService.selectItems(id);
		List<OrderItemData> orderItemsData = new ArrayList<>();
		//convert the pojo to data
		for(OrderItemPojo item: items) {
			OrderItemData o = ObjectUtil.objectMapper(item,OrderItemData.class);
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




}

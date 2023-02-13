package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.increff.pos.model.StatusReport;
import com.increff.pos.util.ObjectUtil;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

import javax.transaction.Transactional;

@Component
public class InventoryDto {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryData addToInventory(InventoryForm form) throws ApiException {
		//convert form to pojo
		InventoryPojo inventoryPojo = ObjectUtil.objectMapper(form, InventoryPojo.class);
		String barcode = form.getBarcode().toLowerCase();
		//get the product of the relevant barcode
		ProductPojo product = productService.selectByBarcode(barcode);
		//set id of the product to the inventory pojo
		inventoryPojo.setProductId(product.getId());
		//add the inventory pojo to the database
		return convert(ObjectUtil.objectMapper(inventoryService.add(inventoryPojo,barcode), InventoryData.class),inventoryPojo);
	}

	public List<StatusReport> addInventories(List<CSVRecord> records, HashMap<String, Integer> headerMap){
		List<StatusReport> reportList = new ArrayList<>();
		for(CSVRecord record: records){
			if(record.getRecordNumber()==1)
				continue;
			InventoryForm inventory = new InventoryForm();
			inventory.setBarcode(record.get(headerMap.get("barcode")));
			inventory.setQuantity(Integer.parseInt(record.get(headerMap.get("quantity"))));
			System.out.println(inventory.getQuantity());
			try{
				addToInventory(inventory);
				System.out.println("added");
			}
			catch(ApiException e){
				System.out.println(e.getMessage());
				StatusReport report = new StatusReport();
				report.setBarcode(inventory.getBarcode());
				report.setQuantity(inventory.getQuantity());
				report.setMessage(e.getMessage());
				reportList.add(report);
			}
		}
		return reportList;
	}
	
	public List<InventoryData> getInventory() throws ApiException{
		//get all the inventory pojos from the database
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<InventoryData> inventoryData = new ArrayList<>();
		//convert each pojo to data and add to the list
		for(InventoryPojo item: inventory) {
			inventoryData.add(convert(ObjectUtil.objectMapper(item,InventoryData.class),item));
		}
		return inventoryData;
	}
	
	public InventoryData getById(Integer id) throws ApiException {
		//get the inventory pojo from the database and convert it to data
		return ObjectUtil.objectMapper(inventoryService.selectByInvId(id), InventoryData.class);
	}

	public InventoryData getByProductId(Integer id) throws ApiException{
		return ObjectUtil.objectMapper(inventoryService.selectById(id), InventoryData.class);
	}
	
	public void editInventory(Integer id, InventoryForm form) throws ApiException {
		//convert form to pojo
		InventoryPojo inventory = ObjectUtil.objectMapper(form,InventoryPojo.class);
		inventory.setId(id);
		String barcode = form.getBarcode().toLowerCase();
		inventoryService.update(inventory, barcode);
	}

	//function to convert InventoryPojo to InventoryData by querying the product table
	public InventoryData convert(InventoryData i, InventoryPojo i2) throws ApiException {
		ProductPojo p = productService.selectById(i2.getProductId());
		i.setBarcode(p.getBarcode());
		i.setName(p.getName());
		return i;
	}

	//function to get all the barcodes of the products in the inventory
	public List<String> getBarcodes() throws ApiException{
		List<InventoryPojo> inventory = inventoryService.selectInventory();
		List<String> barcodes = new ArrayList<>();
		for(InventoryPojo item: inventory){
			barcodes.add(productService.selectById(item.getProductId()).getBarcode());
		}
		return barcodes;
	}

}

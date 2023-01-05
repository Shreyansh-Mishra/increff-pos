package com.increff.employee.model;

public class ProductForm {
	private String barcode;
	private String brandName;
	private String category;
	private String name;
	private double mrp;
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String brandCategory) {
		this.category = brandCategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	
}

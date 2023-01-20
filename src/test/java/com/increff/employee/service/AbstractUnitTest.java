package com.increff.employee.service;

import javax.transaction.Transactional;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {
    public BrandPojo createBrand(String brandName, String category) throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand(brandName);
        p.setCategory(category);
        return p;
    }

    public BrandForm createBrandForm(String brandName, String category) {
        BrandForm p = new BrandForm();
        p.setBrand(brandName);
        p.setCategory(category);
        return p;
    }

    public ProductPojo createProduct(BrandPojo brand, String name, String barcode, double mrp) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        return p;
    }

    public ProductForm createProductForm(BrandForm brand, String name, String barcode, double mrp) {
        ProductForm p = new ProductForm();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        return p;
    }

    public InventoryForm createInventoryForm(String brandName, String category, String name, String barcode, double mrp, int quantity) {
        InventoryForm p = new InventoryForm();
        p.setBarcode(barcode);
        p.setQuantity(quantity);
        return p;
    }

    public InventoryPojo createInventory(ProductPojo product, int quantity){
        InventoryPojo i = new InventoryPojo();
        i.setBarcode(product.getBarcode());
        i.setQuantity(quantity);
        return i;
    }

    public OrderPojo createOrder(){
        OrderPojo o = new OrderPojo();
        return o;
    }

    public OrderItemPojo createOrderItem(ProductPojo product, OrderPojo order, int quantity, double sellingPrice){
        OrderItemPojo oi = new OrderItemPojo();
        oi.setBarcode(product.getBarcode());
        oi.setProductId(product.getId());
        oi.setOrderId(order.getId());
        oi.setQuantity(quantity);
        oi.setSellingPrice(sellingPrice);
        return oi;
    }

}

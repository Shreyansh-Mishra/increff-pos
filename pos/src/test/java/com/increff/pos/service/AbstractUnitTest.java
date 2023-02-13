package com.increff.pos.service;

import javax.transaction.Transactional;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.EditProductForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.*;
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

    public ProductPojo createProduct(BrandPojo brand, String name, String barcode, Double mrp) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        p.setBrand_category(brand.getId());
        return p;
    }

    public ProductForm createProductForm(BrandPojo brand, String name, String barcode, Double mrp) {
        ProductForm p = new ProductForm();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        return p;
    }

    public EditProductForm createEditProductForm(BrandPojo brand, String name, Double mrp){
        EditProductForm p = new EditProductForm();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        return p;
    }

    public InventoryForm createInventoryForm(String barcode, Integer quantity) {
        InventoryForm p = new InventoryForm();
        p.setBarcode(barcode);
        p.setQuantity(quantity);
        return p;
    }

    public InventoryPojo createInventory(ProductPojo product, Integer quantity){
        InventoryPojo i = new InventoryPojo();
        i.setQuantity(quantity);
        i.setProductId(product.getId());
        return i;
    }

    public OrderPojo createOrder(){
        OrderPojo o = new OrderPojo();
        return o;
    }

    public OrderItemPojo createOrderItem(ProductPojo product, OrderPojo order, Integer quantity, Double sellingPrice){
        OrderItemPojo oi = new OrderItemPojo();
        oi.setProductId(product.getId());
        oi.setOrderId(order.getId());
        oi.setQuantity(quantity);
        oi.setMrp(sellingPrice);
        return oi;
    }


}

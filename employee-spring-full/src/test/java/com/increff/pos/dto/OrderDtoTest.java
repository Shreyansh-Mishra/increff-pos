package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.service.AbstractUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    BrandDto brandDto;

    @Autowired
    ProductDto productDto;

    @Autowired
    InventoryDto inventoryDto;

    @Autowired
    OrderDto orderDto;

    @Test
    public void testCreateOrder() throws Exception {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productDto.createProduct(product);
        ProductForm product2 = createProductForm(brand, "testProduct2", "testBarcode2", 200);
        productDto.createProduct(product2);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        InventoryForm inventoryForm1 = createInventoryForm(product2.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm1);
        OrderForm o = new OrderForm();
        o.setBarcode(product.getBarcode());
        o.setQuantity(10);
        o.setMrp(100);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(10);
        o2.setMrp(200);
        List<OrderForm> order = new ArrayList<>();
        order.add(o);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderData> orders = orderDto.getOrders();
        List<OrderItemData> orderItems = orderDto.getOrderItems(orders.get(0).getId());
        assertEquals(1,orders.size());
        assertEquals(2,orderItems.size());
        assertEquals(10,orderItems.get(0).getQuantity(),0);
        assertEquals(10,orderItems.get(1).getQuantity(),0);
        assertEquals(100,orderItems.get(0).getMrp(),0);
        assertEquals(200,orderItems.get(1).getMrp(),0);
    }

    @Test
    public void testGetInvoice() throws Exception {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productDto.createProduct(product);
        ProductForm product2 = createProductForm(brand, "testProduct2", "testBarcode2", 200);
        productDto.createProduct(product2);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        InventoryForm inventoryForm1 = createInventoryForm(product2.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm1);
        OrderForm o = new OrderForm();
        o.setBarcode(product.getBarcode());
        o.setQuantity(10);
        o.setMrp(100);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(10);
        o2.setMrp(200);
        List<OrderForm> order = new ArrayList<>();
        order.add(o);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderData> orders = orderDto.getOrders();
        List<OrderItemData> orderItems = orderDto.getOrderItems(orders.get(0).getId());
        InvoicePojo invoiceData = orderDto.getInvoice(orders.get(0).getId());
        assertEquals(orders.get(0).getId(),invoiceData.getId());
    }
}

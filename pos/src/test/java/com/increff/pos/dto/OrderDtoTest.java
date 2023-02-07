package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    OrderDto orderDto;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    public void testCreateOrder() throws Exception {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        ProductPojo productPojo2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        productDao.insert(productPojo2);
        InventoryPojo inventoryPojo = createInventory(productPojo, 100);
        inventoryDao.insert(inventoryPojo);
        InventoryPojo inventoryPojo2 = createInventory(productPojo2, 100);
        inventoryDao.insert(inventoryPojo2);
        OrderForm o = new OrderForm();
        o.setBarcode(productPojo.getBarcode());
        o.setQuantity(10);
        o.setMrp(100.0);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(productPojo2.getBarcode());
        o2.setQuantity(10);
        o2.setMrp(200.0);
        List<OrderForm> order = new ArrayList<>();
        order.add(o);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderPojo> orders = orderDao.selectAll();
        List<OrderItemPojo> orderItems = orderItemDao.selectItems(orders.get(0).getId());
        assertEquals(1,orders.size());
        assertEquals(2,orderItems.size());
        assertEquals(10,orderItems.get(0).getQuantity(),0);
        assertEquals(10,orderItems.get(1).getQuantity(),0);
        assertEquals(100,orderItems.get(0).getMrp(),0);
        assertEquals(200,orderItems.get(1).getMrp(),0);
    }

    @Test
    public void testGetOrderItems() throws Exception {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        ProductPojo productPojo2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        productDao.insert(productPojo2);
        InventoryPojo inventoryPojo = createInventory(productPojo, 100);
        inventoryDao.insert(inventoryPojo);
        InventoryPojo inventoryPojo2 = createInventory(productPojo2, 100);
        inventoryDao.insert(inventoryPojo2);
        OrderForm o = new OrderForm();
        o.setBarcode(productPojo.getBarcode());
        o.setQuantity(10);
        o.setMrp(100.0);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(productPojo2.getBarcode());
        o2.setQuantity(10);
        o2.setMrp(200.0);
        List<OrderForm> order = new ArrayList<>();
        order.add(o);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderPojo> orders = orderDao.selectAll();
        List<OrderItemData> orderItems = orderDto.getOrderItems(orders.get(0).getId());
        assertEquals(2,orderItems.size());
        assertEquals(10,orderItems.get(0).getQuantity(),0);
        assertEquals(10,orderItems.get(1).getQuantity(),0);
        assertEquals(100,orderItems.get(0).getMrp(),0);
        assertEquals(200,orderItems.get(1).getMrp(),0);
    }

    @Test
    public void testGetInvoice() throws Exception {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        ProductPojo productPojo2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        productDao.insert(productPojo2);
        InventoryPojo inventoryPojo = createInventory(productPojo, 100);
        inventoryDao.insert(inventoryPojo);
        InventoryPojo inventoryPojo2 = createInventory(productPojo2, 100);
        inventoryDao.insert(inventoryPojo2);
        OrderForm o = new OrderForm();
        o.setBarcode(productPojo.getBarcode());
        o.setQuantity(10);
        o.setMrp(100.0);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(productPojo2.getBarcode());
        o2.setQuantity(10);
        o2.setMrp(200.0);
        List<OrderForm> order = new ArrayList<>();
        order.add(o);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderData> orders = orderDto.getOrders();
        InvoicePojo invoiceData = orderDto.getInvoice(orders.get(0).getId());
        assertEquals(orders.get(0).getId(),invoiceData.getId());
    }
}

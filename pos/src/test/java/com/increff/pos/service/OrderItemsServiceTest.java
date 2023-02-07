package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrderItemsServiceTest extends AbstractUnitTest{
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testAddItems() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brandPojo, "name2", "barcode2", 200.0);
        productDao.insert(product2);
        InventoryPojo inventory = createInventory(product, 100);
        InventoryPojo inventory2 = createInventory(product2, 200);
        inventoryDao.insert(inventory);
        inventoryDao.insert(inventory2);
        OrderPojo order = createOrder();
        orderDao.insert(order);
        OrderPojo or = orderDao.selectAll().get(0);
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100.0);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product2,or, 20, 200.0);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(or.getId());
        assertEquals(2, items.size());
        assertEquals((Integer)10, items.get(0).getQuantity());
        assertEquals((Integer) 20, items.get(1).getQuantity());
        assertEquals(100, items.get(0).getMrp(), 0.001);
        assertEquals(200, items.get(1).getMrp(), 0.001);
    }

    @Test
    public void testSelectItems() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brandPojo, "name2", "barcode2", 200.0);
        productDao.insert(product2);
        InventoryPojo inventory = createInventory(product, 100);
        InventoryPojo inventory2 = createInventory(product2, 200);
        inventoryDao.insert(inventory);
        inventoryDao.insert(inventory2);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderDao.insert(order);
        OrderPojo or = orderDao.selectAll().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100.0);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product2,or, 20, 200.0);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(or.getId());
        assertEquals(2, items.size());
        assertEquals((Integer) 10, items.get(0).getQuantity());
        assertEquals((Integer) 20, items.get(1).getQuantity());
        assertEquals(100, items.get(0).getMrp(), 0.001);
        assertEquals(200, items.get(1).getMrp(), 0.001);
    }

    @Test(expected = ApiException.class)
    public void testNegativeQuantity() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderDao.insert(order);
        OrderPojo or = orderDao.selectAll().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, -10, 100.0);
        orderItems.add(item1);
        orderItemsService.addItems(orderItems, or.getId());
    }

    @Test(expected = ApiException.class)
    public void testNegativeSellingPrice() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderDao.insert(order);
        OrderPojo or = orderDao.selectAll().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, -100.0);
        orderItems.add(item1);
        orderItemsService.addItems(orderItems, or.getId());
    }

    @Test(expected = ApiException.class)
    public void testEmptyOrder() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderDao.insert(order);
        OrderPojo or = orderDao.selectAll().get(0);
        orderItemsService.addItems(orderItems, or.getId());
    }

    @Test(expected = ApiException.class)
    public void testSameProductWithDifferentSellingPrice() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        OrderPojo order = createOrder();
        orderDao.insert(order);
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        OrderPojo or = orderDao.selectAll().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100.0);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product,or, 20, 200.0);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
    }

    @Test
    public void testSameProductWithDifferentQuantityButSameSellingPrice() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        OrderPojo order = createOrder();
        orderDao.insert(order);
        List<OrderItemPojo> orderItems =  new ArrayList<>();
        OrderPojo or = orderDao.selectAll().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100.0);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product,or, 20, 100.0);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(or.getId());
        assertEquals(1, items.size());
        assertEquals((Integer) 30, items.get(0).getQuantity());
        assertEquals(100, items.get(0).getMrp(), 0.001);
    }
}

package com.increff.pos.service;

import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrderItemsServiceTest extends AbstractUnitTest{

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderItemsService orderItemsService;

    @Test
    public void testAddItems() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100);
        productService.add(product);
        ProductPojo product2 = createProduct(brandPojo, "name2", "barcode2", 200);
        productService.add(product2);
        InventoryPojo inventory = createInventory(product, 100);
        InventoryPojo inventory2 = createInventory(product2, 200);
        inventoryService.add(inventory);
        inventoryService.add(inventory2);
        OrderPojo order = createOrder();
        orderService.addOrder(order);
        OrderPojo or = orderService.selectOrders().get(0);
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product2,or, 20, 200);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(or.getId());
        assertEquals(2, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(20, items.get(1).getQuantity());
        assertEquals(100, items.get(0).getMrp(), 0.001);
        assertEquals(200, items.get(1).getMrp(), 0.001);
    }

    @Test
    public void testSelectItems() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100);
        productService.add(product);
        ProductPojo product2 = createProduct(brandPojo, "name2", "barcode2", 200);
        productService.add(product2);
        InventoryPojo inventory = createInventory(product, 100);
        InventoryPojo inventory2 = createInventory(product2, 200);
        inventoryService.add(inventory);
        inventoryService.add(inventory2);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderService.addOrder(order);
        OrderPojo or = orderService.selectOrders().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, 100);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product2,or, 20, 200);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, or.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(or.getId());
        assertEquals(2, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(20, items.get(1).getQuantity());
        assertEquals(100, items.get(0).getMrp(), 0.001);
        assertEquals(200, items.get(1).getMrp(), 0.001);
    }

    @Test
    public void testNegativeQuantity() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderService.addOrder(order);
        OrderPojo or = orderService.selectOrders().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, -10, 100);
        orderItems.add(item1);
        try {
            orderItemsService.addItems(orderItems, or.getId());
            fail();
        }
        catch(ApiException e) {
            assertEquals("Quantity should be a positive value", e.getMessage());
        }
    }

    @Test
    public void testNegativeSellingPrice() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderService.addOrder(order);
        OrderPojo or = orderService.selectOrders().get(0);
        OrderItemPojo item1 = createOrderItem(product,or, 10, -100);
        orderItems.add(item1);
        try {
            orderItemsService.addItems(orderItems, or.getId());
            fail();
        }
        catch(ApiException e) {
            assertEquals("Selling Price needs to be positive", e.getMessage());
        }
    }

    @Test
    public void testEmptyOrder() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory("testbrand","testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        OrderPojo order = createOrder();
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        orderService.addOrder(order);
        OrderPojo or = orderService.selectOrders().get(0);
        try {
            orderItemsService.addItems(orderItems, or.getId());
            fail();
        }
        catch(ApiException e) {
            assertEquals("Please add atleast 1 item to place your order!", e.getMessage());
        }
    }
}

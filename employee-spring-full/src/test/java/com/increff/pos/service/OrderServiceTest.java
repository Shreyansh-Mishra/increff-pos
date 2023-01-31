package com.increff.pos.service;

import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class OrderServiceTest extends AbstractUnitTest{
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
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
        assertEquals(200, items.get(1).getSellingPrice(), 0.001);
    }

//    @Test
//    public void testSelectOrdersBetweenDates() throws ApiException, ParseException {
//        BrandPojo brand = createBrand("brand", "category");
//        brandService.add(brand);
//        ProductPojo product = createProduct(brand, "name", "barcode", 100);
//        ProductPojo product2 = createProduct(brand, "name2", "barcode2", 200);
//        productService.add(product);
//        productService.add(product2);
//        InventoryPojo inventory = createInventory(product, 100);
//        InventoryPojo inventory2 = createInventory(product2, 200);
//        inventoryService.add(inventory);
//        inventoryService.add(inventory2);
//        OrderPojo order = createOrder();
//        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
//        OrderItemPojo item1 = createOrderItem(product,order, 10, 100);
//        orderItems.add(item1);
//        OrderItemPojo item2 = createOrderItem(product2,order, 20, 200);
//        orderItems.add(item2);
//        orderService.addItems(orderItems, order);
//        SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
//        sdf.setTimeZone(TimeZone.getTimeZone("utc"));
//        String date1 = sdf.format(sdf2.parse(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)).toString()));
//        String date2 = sdf.format(sdf2.parse(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)).toString()));
//        Instant from = sdf.parse(date1).toInstant();
//        Instant to = sdf.parse(date2).toInstant();
//        System.out.println("from:"+from);
//        List<OrderPojo> order2 = orderService.selectOrdersBetweenDates(from, to);
//        assertEquals(1, order2.size());
//        List<OrderItemPojo> items = orderService.selectItems(order2.get(0).getId());
//        assertEquals(2, items.size());
//        assertEquals(10, items.get(0).getQuantity());
//        assertEquals(20, items.get(1).getQuantity());
//        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
//        assertEquals(200, items.get(1).getSellingPrice(), 0.001);
//    }

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
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
        assertEquals(200, items.get(1).getSellingPrice(), 0.001);
    }

    @Test
    public void testSelectOrders () throws ApiException {
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
        orderItemsService.addItems(orderItems, or.getId());
        OrderPojo order2 = createOrder();
        orderService.addOrder(order2);
        OrderPojo or2 = orderService.selectOrders().get(0);
        List<OrderItemPojo> orderItems2 =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item2 = createOrderItem(product2,or2, 20, 200);
        orderItems2.add(item2);
        orderItemsService.addItems(orderItems2, or2.getId());
        List<OrderPojo> orders = orderService.selectOrders();
        assertEquals(2, orders.size());
        List<OrderItemPojo> items = orderItemsService.selectItems(orders.get(0).getId());
        assertEquals(1, items.size());
        assertEquals(20, items.get(0).getQuantity());
        assertEquals(200, items.get(0).getSellingPrice(), 0.001);
        items = orderItemsService.selectItems(orders.get(1).getId());
        assertEquals(1, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
    }

    @Test
    public void testSelectOrderById() throws ApiException {
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
        orderItemsService.addItems(orderItems, or.getId());
        OrderPojo order2 = createOrder();
        orderService.addOrder(order2);
        OrderPojo or2 = orderService.selectOrders().get(0);
        List<OrderItemPojo> orderItems2 =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item2 = createOrderItem(product2,or2, 20, 200);
        orderItems2.add(item2);
        orderItemsService.addItems(orderItems2, or2.getId());
        OrderPojo order3 = orderService.selectOrderById(or.getId());
        assertEquals(order.getId(), order3.getId());
        List<OrderItemPojo> items = orderItemsService.selectItems(order3.getId());
        assertEquals(1, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
        order3 = orderService.selectOrderById(order2.getId());
        assertEquals(order2.getId(), order3.getId());
        items = orderItemsService.selectItems(order3.getId());
        assertEquals(1, items.size());
        assertEquals(20, items.get(0).getQuantity());
        assertEquals(200, items.get(0).getSellingPrice(), 0.001);
    }

    @Test
    public void testSelectByDate() throws ApiException {
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
        orderItemsService.addItems(orderItems, or.getId());
        OrderPojo order2 = createOrder();
        orderService.addOrder(order2);
        OrderPojo or2 = orderService.selectOrders().get(0);
        List<OrderItemPojo> orderItems2 =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item2 = createOrderItem(product2,or2, 20, 200);
        orderItems2.add(item2);
        orderItemsService.addItems(orderItems2, or2.getId());
        Instant instant = Instant.now();
        List<OrderPojo> orders = orderService.selectByDate(instant);
        assertEquals(2, orders.size());
        List<OrderItemPojo> items = orderItemsService.selectItems(orders.get(0).getId());
        assertEquals(1, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
        items = orderItemsService.selectItems(orders.get(1).getId());
        assertEquals(1, items.size());
        assertEquals(20, items.get(0).getQuantity());
        assertEquals(200, items.get(0).getSellingPrice(), 0.001);
    }

}

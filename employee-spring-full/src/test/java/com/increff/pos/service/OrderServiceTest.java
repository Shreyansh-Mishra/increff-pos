package com.increff.pos.service;

import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    public BrandPojo insertBrand(String brandName, String category) throws ApiException {
        BrandPojo brand = createBrand(brandName, category);
        brandService.add(brand);
        return brandService.selectByNameAndCategory(brandName, category);
    }

    public ProductPojo insertProduct(BrandPojo brand, String productName, String barcode, double mrp) throws ApiException {
        ProductPojo productPojo = createProduct(brand, productName, barcode, mrp);
        productService.add(productPojo);
        return productService.selectByBarcode(barcode);
    }


    public void insertInventory(ProductPojo product, int quantity) throws ApiException {
        InventoryPojo inventory = createInventory(product, quantity);
        inventoryService.add(inventory);
    }

    @Test
    public void testAddOrder() throws ApiException {
        Instant instant = Instant.now();
        OrderPojo order = new OrderPojo();
        orderService.addOrder(order);
        List<OrderPojo> orders = orderService.selectOrders();
        assertEquals(1, orders.size());
        assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), orders.get(0).getTime().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testSelectOrdersBetweenDates() throws ApiException, ParseException {
        BrandPojo brand = insertBrand("testbrand", "testcategory");
        ProductPojo product = insertProduct(brand, "name", "barcode", 100);
        ProductPojo product2 = insertProduct(brand, "name2", "barcode2", 200);
        insertInventory(product, 100);
        insertInventory(product2, 200);

        OrderPojo order = createOrder();
        orderService.addOrder(order);
        List<OrderItemPojo> orderItems =  new ArrayList<OrderItemPojo>();
        OrderItemPojo item1 = createOrderItem(product,order, 10, 100);
        orderItems.add(item1);
        OrderItemPojo item2 = createOrderItem(product2,order, 20, 200);
        orderItems.add(item2);
        orderItemsService.addItems(orderItems, order.getId());
        Instant from = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant to = Instant.now().plus(1, ChronoUnit.DAYS);
        List<OrderPojo> order2 = orderService.selectOrdersBetweenDates(from, to);
        assertEquals(1, order2.size());
        List<OrderItemPojo> items = orderItemsService.selectItems(order2.get(0).getId());
        assertEquals(2, items.size());
        assertEquals(10, items.get(0).getQuantity());
        assertEquals(20, items.get(1).getQuantity());
        assertEquals(100, items.get(0).getSellingPrice(), 0.001);
        assertEquals(200, items.get(1).getSellingPrice(), 0.001);
    }


    @Test
    public void testSelectOrders () throws ApiException {
        BrandPojo brand = insertBrand("testbrand", "testcategory");
        ProductPojo product = insertProduct(brand, "name", "barcode", 100);
        ProductPojo product2 = insertProduct(brand, "name2", "barcode2", 200);
        insertInventory(product, 100);
        insertInventory(product2, 200);
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
        BrandPojo brand = insertBrand("testbrand", "testcategory");
        ProductPojo product = insertProduct(brand, "name", "barcode", 100);
        ProductPojo product2 = insertProduct(brand, "name2", "barcode2", 200);
        insertInventory(product, 100);
        insertInventory(product2, 200);
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
        try{
            orderService.selectOrderById(100);
            fail();
        } catch (ApiException e) {
            assertEquals("Order with id 100 does not exist", e.getMessage());
        }
    }

    @Test
    public void testSelectByDate() throws ApiException {
        BrandPojo brand = insertBrand("testbrand", "testcategory");
        ProductPojo product = insertProduct(brand, "name", "barcode", 100);
        ProductPojo product2 = insertProduct(brand, "name2", "barcode2", 200);
        insertInventory(product, 100);
        insertInventory(product2, 200);
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
        Instant from = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        Instant to = Instant.now().plus(1,ChronoUnit.DAYS).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        List<OrderPojo> orders = orderService.selectByDate(from, to);
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

package com.increff.pos.dto;

import com.increff.pos.dao.*;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    private ReportDto reportDto;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testInventoryReport() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        productDao.insert(product2);
        BrandPojo brand2 = createBrand("testbrand2", "testcategory2");
        brandDao.insert(brand2);
        ProductPojo product3 = createProduct(brand2, "testproduct3", "testbarcode3", 300.0);
        productDao.insert(product3);
        BrandPojo brand3 = createBrand("testbrand3", "testcategory3");
        brandDao.insert(brand3);
        ProductPojo product4 = createProduct(brand3, "testproduct4", "testbarcode4", 400.0);
        productDao.insert(product4);
        ProductPojo product5 = createProduct(brand3, "testproduct5", "testbarcode5", 500.0);
        productDao.insert(product5);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        InventoryPojo inventory1 = createInventory(product2, 150);
        inventoryDao.insert(inventory1);
        InventoryPojo inventory2 = createInventory(product3, 200);
        inventoryDao.insert(inventory2);
        InventoryPojo inventory3 = createInventory(product4, 250);
        inventoryDao.insert(inventory3);
        InventoryPojo inventory4 = createInventory(product5, 300);
        inventoryDao.insert(inventory4);
        List<InventoryReportData> inventoryReportData = reportDto.getInventoryReport();
        assertEquals(3, inventoryReportData.size());
        assertEquals(brand.getBrand().toLowerCase(), inventoryReportData.get(0).getBrand());
        assertEquals(brand.getCategory().toLowerCase(), inventoryReportData.get(0).getCategory());
        assertEquals((Integer) 250,inventoryReportData.get(0).getQuantity());
        assertEquals(brand2.getBrand().toLowerCase(), inventoryReportData.get(1).getBrand());
        assertEquals(brand2.getCategory().toLowerCase(), inventoryReportData.get(1).getCategory());
        assertEquals((Integer) 200,inventoryReportData.get(1).getQuantity());
        assertEquals(brand3.getBrand().toLowerCase(), inventoryReportData.get(2).getBrand());
        assertEquals(brand3.getCategory().toLowerCase(), inventoryReportData.get(2).getCategory());
        assertEquals((Integer) 550,inventoryReportData.get(2).getQuantity());
    }

    @Test
    public void testGetSalesByBrandAndCategory() throws Exception {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        productDao.insert(product2);
        BrandPojo brand2 = createBrand("testbrand2", "testcategory2");
        brandDao.insert(brand2);
        ProductPojo product3 = createProduct(brand2, "testproduct3", "testbarcode3", 300.0);
        productDao.insert(product3);
        BrandPojo brand3 = createBrand("testbrand3", "testcategory3");
        brandDao.insert(brand3);
        ProductPojo product4 = createProduct(brand3, "testproduct4", "testbarcode4", 400.0);
        productDao.insert(product4);
        ProductPojo product5 = createProduct(brand3, "testproduct5", "testbarcode5", 500.0);
        productDao.insert(product5);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        InventoryPojo inventory1 = createInventory(product2, 150);
        inventoryDao.insert(inventory1);
        InventoryPojo inventory2 = createInventory(product3, 200);
        inventoryDao.insert(inventory2);
        InventoryPojo inventory3 = createInventory(product4, 250);
        inventoryDao.insert(inventory3);
        InventoryPojo inventory4 = createInventory(product5, 300);
        inventoryDao.insert(inventory4);
        List<OrderForm> order = new ArrayList<>();
        OrderForm o1 = new OrderForm();
        o1.setBarcode(product.getBarcode());
        o1.setQuantity(13);
        o1.setMrp((double) 100);
        order.add(o1);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(14);
        o2.setMrp((double) 200);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderForm> order2 = new ArrayList<>();
        OrderForm o3 = new OrderForm();
        o3.setBarcode(product3.getBarcode());
        o3.setQuantity(15);
        o3.setMrp((double) 300);
        order2.add(o3);
        orderDto.createOrder(order2);
        List<OrderForm> order3 = new ArrayList<>();
        OrderForm o4 = new OrderForm();
        o4.setBarcode(product4.getBarcode());
        o4.setQuantity(16);
        o4.setMrp((double) 400);
        order3.add(o4);
        OrderForm o5 = new OrderForm();
        o5.setBarcode(product5.getBarcode());
        o5.setQuantity(17);
        o5.setMrp((double) 500);
        order3.add(o5);
        OrderForm o6 = new OrderForm();
        o6.setBarcode(product.getBarcode());
        o6.setQuantity(18);
        o6.setMrp((double) 100);
        order3.add(o6);
        orderDto.createOrder(order3);
        Instant start = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant end = Instant.now().plus(1, ChronoUnit.DAYS);
        List<SalesByBrandAndCategoryData> salesReportData = reportDto.getSalesByBrandAndCategory(start.toString(), end.toString(),"all","all");
        assertEquals(3, salesReportData.size());
        assertEquals(brand.getBrand().toLowerCase(), salesReportData.get(0).getBrand());
        assertEquals(brand.getCategory().toLowerCase(), salesReportData.get(0).getCategory());
        assertEquals((Integer) 45,salesReportData.get(0).getQuantity());
        assertEquals(product.getMrp()*31+product2.getMrp()*14,salesReportData.get(0).getRevenue(),0.1);
        assertEquals(brand2.getBrand().toLowerCase(), salesReportData.get(1).getBrand());
        assertEquals(brand2.getCategory().toLowerCase(), salesReportData.get(1).getCategory());
        assertEquals((Integer) 15,salesReportData.get(1).getQuantity());
        assertEquals(product3.getMrp()*15,salesReportData.get(1).getRevenue(),0.1);
        assertEquals(brand3.getBrand().toLowerCase(), salesReportData.get(2).getBrand());
        assertEquals(brand3.getCategory().toLowerCase(), salesReportData.get(2).getCategory());
        assertEquals((Integer) 33,salesReportData.get(2).getQuantity());
        assertEquals(product4.getMrp()*16+product5.getMrp()*17,salesReportData.get(2).getRevenue(),0.1);
    }


    @Test
    public void testScheduler() throws Exception {
        BrandPojo brand =  createBrand("testbrand", "testcategory");
        BrandPojo brand2 = createBrand("testbrand2", "testcategory2");
        BrandPojo brand3 = createBrand("testbrand3", "testcategory3");
        brandDao.insert(brand);
        brandDao.insert(brand2);
        brandDao.insert(brand3);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode", 100.0);
        ProductPojo product2 = createProduct(brand, "testproduct2", "testbarcode2", 200.0);
        ProductPojo product3 = createProduct(brand2, "testproduct3", "testbarcode3", 300.0);
        ProductPojo product4 = createProduct(brand3, "testproduct4", "testbarcode4", 400.0);
        ProductPojo product5 = createProduct(brand3, "testproduct5", "testbarcode5", 500.0);
        productDao.insert(product);
        productDao.insert(product2);
        productDao.insert(product3);
        productDao.insert(product4);
        productDao.insert(product5);
        InventoryPojo inventory = createInventory(product, 100);
        InventoryPojo inventory1 = createInventory(product2, 150);
        InventoryPojo inventory2 = createInventory(product3, 200);
        InventoryPojo inventory3 = createInventory(product4, 250);
        InventoryPojo inventory4 = createInventory(product5, 300);
        inventoryDao.insert(inventory);
        inventoryDao.insert(inventory1);
        inventoryDao.insert(inventory2);
        inventoryDao.insert(inventory3);
        inventoryDao.insert(inventory4);
        List<OrderForm> order = new ArrayList<>();
        OrderForm o1 = new OrderForm();
        o1.setBarcode(product.getBarcode());
        o1.setQuantity(13);
        o1.setMrp(100.0);
        order.add(o1);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(14);
        o2.setMrp(200.0);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderForm> order2 = new ArrayList<>();
        OrderForm o3 = new OrderForm();
        o3.setBarcode(product3.getBarcode());
        o3.setQuantity(15);
        o3.setMrp(300.0);
        order2.add(o3);
        orderDto.createOrder(order2);
        List<OrderForm> order3 = new ArrayList<>();
        OrderForm o4 = new OrderForm();
        o4.setBarcode(product4.getBarcode());
        o4.setQuantity(16);
        o4.setMrp(400.0);
        order3.add(o4);
        OrderForm o5 = new OrderForm();
        o5.setBarcode(product5.getBarcode());
        o5.setQuantity(17);
        o5.setMrp(500.0);
        order3.add(o5);
        OrderForm o6 = new OrderForm();
        o6.setBarcode(product.getBarcode());
        o6.setQuantity(18);
        o6.setMrp(100.0);
        order3.add(o6);
        orderDto.createOrder(order3);
        reportDto.updateScheduler();
        Instant from = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant to = Instant.now().plus(1, ChronoUnit.DAYS);
        List<DayWiseReportData> dayWiseReportData = reportDto.getDayWiseReport(from.toString(),to.toString());
        assertEquals(1, dayWiseReportData.size());
        assertEquals((Integer) 3, dayWiseReportData.get(0).getInvoiced_orders_count());
        assertEquals((Integer) 93, dayWiseReportData.get(0).getInvoiced_items_count());
        assertEquals(100*13+200*14+300*15+400*16+500*17+100*18, dayWiseReportData.get(0).getTotal_revenue(), 0.1);
    }



}

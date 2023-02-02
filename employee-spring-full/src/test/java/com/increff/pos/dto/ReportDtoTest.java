package com.increff.pos.dto;

import com.increff.pos.model.*;
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
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderDto orderDto;

    @Test
    public void testInventoryReport() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productDto.createProduct(product);
        ProductForm product2 = createProductForm(brand, "testProduct2", "testBarcode2", 200);
        productDto.createProduct(product2);
        BrandForm brand2 = createBrandForm("testBrand2", "testCategory2");
        brandDto.createBrand(brand2);
        ProductForm product3 = createProductForm(brand2, "testProduct3", "testBarcode3", 300);
        productDto.createProduct(product3);
        BrandForm brand3 = createBrandForm("testBrand3", "testCategory3");
        brandDto.createBrand(brand3);
        ProductForm product4 = createProductForm(brand3, "testProduct4", "testBarcode4", 400);
        productDto.createProduct(product4);
        ProductForm product5 = createProductForm(brand3, "testProduct5", "testBarcode5", 500);
        productDto.createProduct(product5);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        InventoryForm inventoryForm1 = createInventoryForm(product2.getBarcode(), 150);
        inventoryDto.addToInventory(inventoryForm1);
        InventoryForm inventoryForm2 = createInventoryForm(product3.getBarcode(), 200);
        inventoryDto.addToInventory(inventoryForm2);
        InventoryForm inventoryForm3 = createInventoryForm(product4.getBarcode(), 250);
        inventoryDto.addToInventory(inventoryForm3);
        InventoryForm inventoryForm4 = createInventoryForm(product5.getBarcode(), 300);
        inventoryDto.addToInventory(inventoryForm4);
        List<InventoryReportData> inventoryReportData = reportDto.getInventoryReport();
        assertEquals(3, inventoryReportData.size());
        assertEquals(brand.getBrand().toLowerCase(), inventoryReportData.get(0).getBrand());
        assertEquals(brand.getCategory().toLowerCase(), inventoryReportData.get(0).getCategory());
        assertEquals(250,inventoryReportData.get(0).getQuantity());
        assertEquals(brand2.getBrand().toLowerCase(), inventoryReportData.get(1).getBrand());
        assertEquals(brand2.getCategory().toLowerCase(), inventoryReportData.get(1).getCategory());
        assertEquals(200,inventoryReportData.get(1).getQuantity());
        assertEquals(brand3.getBrand().toLowerCase(), inventoryReportData.get(2).getBrand());
        assertEquals(brand3.getCategory().toLowerCase(), inventoryReportData.get(2).getCategory());
        assertEquals(550,inventoryReportData.get(2).getQuantity());
    }

    @Test
    public void testGetSalesByBrandAndCategory() throws Exception {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        BrandForm brand2 = createBrandForm("testBrand2", "testCategory2");
        brandDto.createBrand(brand2);
        BrandForm brand3 = createBrandForm("testBrand3", "testCategory3");
        brandDto.createBrand(brand3);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productDto.createProduct(product);
        ProductForm product2 = createProductForm(brand, "testProduct2", "testBarcode2", 200);
        productDto.createProduct(product2);
        ProductForm product3 = createProductForm(brand2, "testProduct3", "testBarcode3", 300);
        productDto.createProduct(product3);
        ProductForm product4 = createProductForm(brand3, "testProduct4", "testBarcode4", 400);
        productDto.createProduct(product4);
        ProductForm product5 = createProductForm(brand3, "testProduct5", "testBarcode5", 500);
        productDto.createProduct(product5);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        InventoryForm inventoryForm1 = createInventoryForm(product2.getBarcode(), 150);
        inventoryDto.addToInventory(inventoryForm1);
        InventoryForm inventoryForm2 = createInventoryForm(product3.getBarcode(), 200);
        inventoryDto.addToInventory(inventoryForm2);
        InventoryForm inventoryForm3 = createInventoryForm(product4.getBarcode(), 250);
        inventoryDto.addToInventory(inventoryForm3);
        InventoryForm inventoryForm4 = createInventoryForm(product5.getBarcode(), 300);
        inventoryDto.addToInventory(inventoryForm4);
        List<OrderForm> order = new ArrayList<>();
        OrderForm o1 = new OrderForm();
        o1.setBarcode(product.getBarcode());
        o1.setQuantity(13);
        o1.setMrp(100);
        order.add(o1);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(14);
        o2.setMrp(200);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderForm> order2 = new ArrayList<>();
        OrderForm o3 = new OrderForm();
        o3.setBarcode(product3.getBarcode());
        o3.setQuantity(15);
        o3.setMrp(300);
        order2.add(o3);
        orderDto.createOrder(order2);
        List<OrderForm> order3 = new ArrayList<>();
        OrderForm o4 = new OrderForm();
        o4.setBarcode(product4.getBarcode());
        o4.setQuantity(16);
        o4.setMrp(400);
        order3.add(o4);
        OrderForm o5 = new OrderForm();
        o5.setBarcode(product5.getBarcode());
        o5.setQuantity(17);
        o5.setMrp(500);
        order3.add(o5);
        OrderForm o6 = new OrderForm();
        o6.setBarcode(product.getBarcode());
        o6.setQuantity(18);
        o6.setMrp(100);
        order3.add(o6);
        orderDto.createOrder(order3);
        Instant start = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant end = Instant.now().plus(1, ChronoUnit.DAYS);
        List<SalesByBrandAndCategoryData> salesReportData = reportDto.getSalesByBrandAndCategory(start.toString(), end.toString());
        assertEquals(3, salesReportData.size());
        assertEquals(brand.getBrand().toLowerCase(), salesReportData.get(0).getBrand());
        assertEquals(brand.getCategory().toLowerCase(), salesReportData.get(0).getCategory());
        assertEquals(45,salesReportData.get(0).getQuantity());
        assertEquals(product.getMrp()*31+product2.getMrp()*14,salesReportData.get(0).getRevenue(),0.1);
        assertEquals(brand2.getBrand().toLowerCase(), salesReportData.get(1).getBrand());
        assertEquals(brand2.getCategory().toLowerCase(), salesReportData.get(1).getCategory());
        assertEquals(15,salesReportData.get(1).getQuantity());
        assertEquals(product3.getMrp()*15,salesReportData.get(1).getRevenue(),0.1);
        assertEquals(brand3.getBrand().toLowerCase(), salesReportData.get(2).getBrand());
        assertEquals(brand3.getCategory().toLowerCase(), salesReportData.get(2).getCategory());
        assertEquals(33,salesReportData.get(2).getQuantity());
        assertEquals(product4.getMrp()*16+product5.getMrp()*17,salesReportData.get(2).getRevenue(),0.1);
    }


    @Test
    public void testScheduler() throws Exception {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        BrandForm brand2 = createBrandForm("testBrand2", "testCategory2");
        BrandForm brand3 = createBrandForm("testBrand3", "testCategory3");
        brandDto.createBrand(brand);
        brandDto.createBrand(brand2);
        brandDto.createBrand(brand3);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        ProductForm product2 = createProductForm(brand, "testProduct2", "testBarcode2", 200);
        ProductForm product3 = createProductForm(brand2, "testProduct3", "testBarcode3", 300);
        ProductForm product4 = createProductForm(brand3, "testProduct4", "testBarcode4", 400);
        ProductForm product5 = createProductForm(brand3, "testProduct5", "testBarcode5", 500);
        productDto.createProduct(product);
        productDto.createProduct(product2);
        productDto.createProduct(product3);
        productDto.createProduct(product4);
        productDto.createProduct(product5);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        InventoryForm inventoryForm1 = createInventoryForm(product2.getBarcode(), 150);
        InventoryForm inventoryForm2 = createInventoryForm(product3.getBarcode(), 200);
        InventoryForm inventoryForm3 = createInventoryForm(product4.getBarcode(), 250);
        InventoryForm inventoryForm4 = createInventoryForm(product5.getBarcode(), 300);
        inventoryDto.addToInventory(inventoryForm);
        inventoryDto.addToInventory(inventoryForm1);
        inventoryDto.addToInventory(inventoryForm2);
        inventoryDto.addToInventory(inventoryForm3);
        inventoryDto.addToInventory(inventoryForm4);
        List<OrderForm> order = new ArrayList<>();
        OrderForm o1 = new OrderForm();
        o1.setBarcode(product.getBarcode());
        o1.setQuantity(13);
        o1.setMrp(100);
        order.add(o1);
        OrderForm o2 = new OrderForm();
        o2.setBarcode(product2.getBarcode());
        o2.setQuantity(14);
        o2.setMrp(200);
        order.add(o2);
        orderDto.createOrder(order);
        List<OrderForm> order2 = new ArrayList<>();
        OrderForm o3 = new OrderForm();
        o3.setBarcode(product3.getBarcode());
        o3.setQuantity(15);
        o3.setMrp(300);
        order2.add(o3);
        orderDto.createOrder(order2);
        List<OrderForm> order3 = new ArrayList<>();
        OrderForm o4 = new OrderForm();
        o4.setBarcode(product4.getBarcode());
        o4.setQuantity(16);
        o4.setMrp(400);
        order3.add(o4);
        OrderForm o5 = new OrderForm();
        o5.setBarcode(product5.getBarcode());
        o5.setQuantity(17);
        o5.setMrp(500);
        order3.add(o5);
        OrderForm o6 = new OrderForm();
        o6.setBarcode(product.getBarcode());
        o6.setQuantity(18);
        o6.setMrp(100);
        order3.add(o6);
        orderDto.createOrder(order3);
        reportDto.updateScheduler();
        List<DayWiseReportData> dayWiseReportData = reportDto.getDayWiseReport();
        assertEquals(1, dayWiseReportData.size());
        assertEquals(3, dayWiseReportData.get(0).getInvoiced_orders_count());
        assertEquals(93, dayWiseReportData.get(0).getInvoiced_items_count());
        assertEquals(100*13+200*14+300*15+400*16+500*17+100*18, dayWiseReportData.get(0).getTotal_revenue(), 0.1);
    }



}

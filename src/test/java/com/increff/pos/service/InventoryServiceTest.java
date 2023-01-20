package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest{
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand,"testproduct","testbarcode",100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        int id = productService.selectByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        List<InventoryPojo> inventories = inventoryService.selectInventory();
        assertEquals(1, inventories.size());
        assertEquals(inventory.getQuantity(), inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
    }

    @Test
    public void testSelectById() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand,"testproduct","testbarcode",100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        int id = productService.selectByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo inventoryPojo = inventoryService.selectById(id);
        assertEquals(inventory.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode", 100);
        productService.add(product);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        int id = productService.selectByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo i2 = createInventory(product, 200);
        inventoryService.update(i2);
        InventoryPojo inventoryPojo = inventoryService.selectById(id);
        assertEquals(i2.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
        assertEquals(id, inventoryPojo.getId());
    }


}

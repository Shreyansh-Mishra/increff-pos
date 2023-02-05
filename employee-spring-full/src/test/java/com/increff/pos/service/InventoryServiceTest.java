package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InventoryServiceTest extends AbstractUnitTest{
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    public ProductPojo createBrandAndProductPojo(String brandName, String category, String productName, String barcode, Double mrp) throws ApiException {
        BrandPojo brand = createBrand(brandName, category);
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select(brandName.toLowerCase(),category.toLowerCase());
        ProductPojo product = createProduct(brandPojo, productName, barcode, mrp);
        productDao.insert(product);
        ProductPojo productPojo = productDao.selectBarcode(barcode);
        return productPojo;
    }

    @Test
    public void testAdd() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        Integer id = productDao.selectBrandAndCategory("testbrand", "testcategory").get(0).getId();
        List<InventoryPojo> inventories = inventoryDao.selectAll();
        assertEquals(1, inventories.size());
        assertEquals(inventory.getQuantity(), inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
        InventoryPojo inventory2 = createInventory(product, 100);
        inventoryService.add(inventory2);
        inventories = inventoryDao.selectAll();
        assertEquals(1, inventories.size());
        assertEquals((Integer) 200, inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
        try{
            InventoryPojo inventory3 = createInventory(product, -100);
            inventoryService.add(inventory3);
            fail();
        }
        catch (ApiException e){
            assertEquals("Enter a valid quantity for product with barcode testbarcode", e.getMessage());
        }
    }

    @Test
    public void testSelectById() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        Integer id = productDao.selectBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo inventoryPojo = inventoryService.selectById(id);
        assertEquals(inventory.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
        try{
            inventoryService.selectById(100);
            fail();
        }
        catch (ApiException e){
            assertEquals("The product does not exists in the Inventory", e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        Integer id = productDao.selectBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo i2 = createInventory(product, 200);
        inventoryService.update(i2);
        InventoryPojo inventoryPojo = inventoryDao.selectId(id);
        assertEquals(i2.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
        assertEquals(id, inventoryPojo.getId());
    }


}

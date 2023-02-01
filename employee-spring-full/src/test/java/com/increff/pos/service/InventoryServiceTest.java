package com.increff.pos.service;

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
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    public ProductPojo createBrandAndProductPojo(String brandName, String category, String productName, String barcode, double mrp) throws ApiException {
        BrandPojo brand = createBrand(brandName, category);
        brandService.add(brand);
        BrandPojo brandPojo = brandService.selectByNameAndCategory(brandName.toLowerCase(),category.toLowerCase());
        ProductPojo product = createProduct(brandPojo, productName, barcode, mrp);
        productService.add(product);
        ProductPojo productPojo = productService.selectByBarcode(barcode);
        return productPojo;
    }

    @Test
    public void testAdd() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        int id = productService.selectByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        List<InventoryPojo> inventories = inventoryService.selectInventory();
        assertEquals(1, inventories.size());
        assertEquals(inventory.getQuantity(), inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
        InventoryPojo inventory2 = createInventory(product, 100);
        inventoryService.add(inventory2);
        inventories = inventoryService.selectInventory();
        assertEquals(1, inventories.size());
        assertEquals(200, inventories.get(0).getQuantity());
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
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory);
        int id = productService.selectByBrandAndCategory("testbrand", "testcategory").get(0).getId();
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
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100);
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

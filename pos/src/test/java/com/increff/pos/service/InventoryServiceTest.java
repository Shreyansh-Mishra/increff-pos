package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.dto.ProductDto;
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

    @Autowired
    private ProductDto productDto;

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
        inventoryService.add(inventory,"testbarcode");
        Integer id = productDto.getProductsByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        List<InventoryPojo> inventories = inventoryDao.selectAll();
        assertEquals(1, inventories.size());
        assertEquals(inventory.getQuantity(), inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
    }

    @Test
    public void testAddDuplicate() throws ApiException{
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryService.add(inventory,"testbarcode");
        inventoryService.add(inventory,"testbarcode");
        Integer id = productDto.getProductsByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        List<InventoryPojo> inventories = inventoryDao.selectAll();
        assertEquals(1, inventories.size());
        assertEquals((Integer) 200, inventories.get(0).getQuantity());
        assertEquals(id, inventories.get(0).getId());
    }

    @Test(expected = ApiException.class)
    public void testNegativeInventory() throws ApiException{
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, -100);
        inventoryService.add(inventory,"testbarcode");
    }

    @Test
    public void testSelectById() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        Integer id = productDto.getProductsByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo inventoryPojo = inventoryService.selectById(id);
        assertEquals(inventory.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void testSelectByIdException() throws ApiException{
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        inventoryService.selectById(100);
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo product = createBrandAndProductPojo("testbrand","testcategory", "testproduct", "testbarcode",100.0);
        InventoryPojo inventory = createInventory(product, 100);
        inventoryDao.insert(inventory);
        Integer id = productDto.getProductsByBrandAndCategory("testbrand", "testcategory").get(0).getId();
        InventoryPojo i2 = createInventory(product, 200);
        inventoryService.update(i2,"testbarcode");
        InventoryPojo inventoryPojo = inventoryDao.selectId(id);
        assertEquals(i2.getQuantity(), inventoryPojo.getQuantity());
        assertEquals(id, inventoryPojo.getId());
        assertEquals(id, inventoryPojo.getId());
    }

    @Test
    public void testSelectInventory() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode", 100.0);
        productDao.insert(product);
        ProductPojo productPojo = productDao.selectBarcode("testbarcode");
        InventoryPojo inventory = createInventory(productPojo, 100);
        inventoryDao.insert(inventory);
        ProductPojo product2 = createProduct(brandPojo, "testproduct2", "testbarcode2", 100.0);
        productDao.insert(product2);
        ProductPojo productPojo2 = productDao.selectBarcode("testbarcode2");
        InventoryPojo inventory2 = createInventory(productPojo2, 100);
        inventoryDao.insert(inventory2);
        List<InventoryPojo> inventories = inventoryService.selectInventory();
        assertEquals(2, inventories.size());
        assertEquals(inventory.getQuantity(), inventories.get(0).getQuantity());
        assertEquals(inventory2.getQuantity(), inventories.get(1).getQuantity());
    }

}

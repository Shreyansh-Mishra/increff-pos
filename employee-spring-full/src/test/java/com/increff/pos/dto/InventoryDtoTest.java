package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testAddToInventory() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        InventoryForm inventoryForm = createInventoryForm(productPojo.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        List<InventoryPojo> inventoryData = inventoryDao.selectAll();
        assertEquals(1,inventoryData.size());
        assertEquals(100,inventoryData.get(0).getQuantity(),0);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo = createInventory(productPojo, 100);
        inventoryDao.insert(inventoryPojo);
        List<InventoryPojo> inventoryData = inventoryDao.selectAll();
        InventoryData inventory = inventoryDto.getById(inventoryData.get(0).getId());
        assertEquals(100,inventory.getQuantity(),0);
    }

    @Test
    public void testEditInventory() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo = createInventory(productPojo, 100);
        inventoryDao.insert(inventoryPojo);
        List<InventoryPojo> inventoryData = inventoryDao.selectAll();
        InventoryForm inventoryForm1 = createInventoryForm(productPojo.getBarcode(), 200);
        inventoryDto.editInventory(inventoryData.get(0).getId(), inventoryForm1);
        InventoryPojo inventory = inventoryDao.selectId(inventoryData.get(0).getId());
        assertEquals(200,inventory.getQuantity(),0);
    }
}

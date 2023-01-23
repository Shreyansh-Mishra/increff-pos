package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    BrandDto brandDto;

    @Autowired
    ProductDto productFlow;

    @Autowired
    InventoryDto inventoryDto;

    @Test
    public void testAddToInventory() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryDto.getInventory();
        assertEquals(1,inventoryData.size());
        assertEquals(100,inventoryData.get(0).getQuantity(),0);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryDto.getInventory();
        InventoryData inventory = inventoryDto.getById(inventoryData.get(0).getId());
        assertEquals(100,inventory.getQuantity(),0);
    }

    @Test
    public void testEditInventory() throws ApiException{
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryDto.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryDto.getInventory();
        InventoryForm inventoryForm1 = createInventoryForm(product.getBarcode(), 200);
        inventoryDto.editInventory(inventoryData.get(0).getId(), inventoryForm1);
        InventoryData inventory = inventoryDto.getById(inventoryData.get(0).getId());
        assertEquals(200,inventory.getQuantity(),0);
    }
}

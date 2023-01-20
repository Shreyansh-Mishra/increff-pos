package com.increff.employee.flow;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryFlowTest extends AbstractUnitTest {

    @Autowired
    BrandFlow brandFlow;

    @Autowired
    ProductFlow productFlow;

    @Autowired
    InventoryFlow inventoryFlow;

    @Test
    public void testAddToInventory() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandFlow.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryFlow.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryFlow.getInventory();
        assertEquals(1,inventoryData.size());
        assertEquals(100,inventoryData.get(0).getQuantity(),0);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandFlow.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryFlow.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryFlow.getInventory();
        InventoryData inventory = inventoryFlow.getById(inventoryData.get(0).getId());
        assertEquals(100,inventory.getQuantity(),0);
    }

    @Test
    public void testEditInventory() throws ApiException{
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandFlow.createBrand(brand);
        ProductForm product = createProductForm(brand, "testProduct", "testBarcode", 100);
        productFlow.createProduct(product);
        InventoryForm inventoryForm = createInventoryForm(product.getBarcode(), 100);
        inventoryFlow.addToInventory(inventoryForm);
        List<InventoryData> inventoryData = inventoryFlow.getInventory();
        InventoryForm inventoryForm1 = createInventoryForm(product.getBarcode(), 200);
        inventoryFlow.editInventory(inventoryData.get(0).getId(), inventoryForm1);
        InventoryData inventory = inventoryFlow.getById(inventoryData.get(0).getId());
        assertEquals(200,inventory.getQuantity(),0);
    }
}

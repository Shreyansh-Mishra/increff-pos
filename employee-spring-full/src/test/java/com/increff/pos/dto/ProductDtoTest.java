package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    BrandDto brandDto;

    @Autowired
    ProductDto productFlow;

    @Test
    public void testCreateProduct() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productFlow.createProduct(form);
        List<ProductData> products = productFlow.getAllProducts();
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testProductsById() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productFlow.createProduct(form);
        List<ProductData> products = productFlow.getAllProducts();
        ProductData product = productFlow.getProductsById(products.get(0).getId());
        assertEquals("testproduct",product.getName());
        assertEquals("testbarcode",product.getBarcode());
        assertEquals(100,product.getMrp(),0);
    }

    @Test
    public void testGetProductByBrandName() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productFlow.createProduct(form);
        List<ProductData> products = productFlow.getProductByBrandName("testBrand");
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testGetProductsByBrandAndCategory() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productFlow.createProduct(form);
        List<ProductData> products = productFlow.getProductsByBrandAndCategory("testBrand","testCategory");
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testUpdateProduct() throws ApiException {
        BrandForm brand = createBrandForm("testBrand", "testCategory");
        brandDto.createBrand(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productFlow.createProduct(form);
        List<ProductData> products = productFlow.getAllProducts();
        ProductForm form1 = createProductForm(brand, "testProduct1", "testBarcode1", 200.0);
        productFlow.updateProduct(products.get(0).getId(),form1);
        List<ProductData> products1 = productFlow.getAllProducts();
        assertEquals(1,products1.size());
        assertEquals("testproduct1",products1.get(0).getName());
        assertEquals("testbarcode1",products1.get(0).getBarcode());
        assertEquals(200,products1.get(0).getMrp(),0);
    }
}

package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
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
    ProductDto productDto;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ProductDao productDao;

    @Test
    public void testCreateProduct() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductForm form = createProductForm(brand, "testProduct", "testBarcode", 100.0);
        productDto.createProduct(form);
        List<ProductPojo> products = productDao.selectAll();
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testGetAllProducts() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        List<ProductData> products = productDto.getAllProducts();
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testGetProductByBarcode() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        ProductData product = productDto.getProductByBarcode("testbarcode");
        assertEquals("testproduct",product.getName());
        assertEquals("testbarcode",product.getBarcode());
        assertEquals(100,product.getMrp(),0);
    }

    @Test
    public void testProductsById() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        List<ProductPojo> products = productDao.selectAll();
        ProductData product = productDto.getProductsById(products.get(0).getId());
        assertEquals("testproduct",product.getName());
        assertEquals("testbarcode",product.getBarcode());
        assertEquals(100,product.getMrp(),0);
    }

    @Test
    public void testGetProductByBrandName() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        List<ProductData> products = productDto.getProductByBrandName("testBrand");
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testGetProductsByBrandAndCategory() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        List<ProductData> products = productDto.getProductsByBrandAndCategory("testBrand","testCategory");
        assertEquals(1,products.size());
        assertEquals("testproduct",products.get(0).getName());
        assertEquals("testbarcode",products.get(0).getBarcode());
        assertEquals(100,products.get(0).getMrp(),0);
    }

    @Test
    public void testUpdateProduct() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandDao.insert(brand);
        ProductPojo productPojo = createProduct(brand, "testproduct", "testbarcode", 100.0);
        productDao.insert(productPojo);
        List<ProductPojo> products = productDao.selectAll();
        EditProductForm form1 = createEditProductForm(brand, "testProduct1", 200.0);
        productDto.updateProduct(products.get(0).getId(),form1);
        List<ProductPojo> products1 = productDao.selectAll();
        assertEquals(1,products1.size());
        assertEquals("testproduct1",products1.get(0).getName());
        assertEquals(200,products1.get(0).getMrp(),0);
    }
}

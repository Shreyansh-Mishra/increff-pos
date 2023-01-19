package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class ProductServiceTest extends AbstractUnitTest{

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand,"testproduct","testbarcode",100);
        productService.add(product);
        List<ProductPojo> products = productService.selectAll();
        assertEquals(1, products.size());
        assertEquals("testbarcode", products.get(0).getBarcode());
        assertEquals("testproduct", products.get(0).getName());
        assertEquals(100.0, products.get(0).getMrp(), 0.0);
        assertEquals("testbrand", products.get(0).getBrandName());
        assertEquals("testcategory", products.get(0).getCategory());
    }

    @Test
    public void testSelectAll() throws ApiException {
        BrandPojo brand = createBrand("testBrand", "testCategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand,"testproduct1","testbarcode1",100);
        productService.add(product);
        ProductPojo product2 = createProduct(brand,"testproduct2","testbarcode2",200);
        productService.add(product2);
        List<ProductPojo> products = productService.selectAll();
        assertEquals(2, products.size());
        assertEquals("testbarcode1", products.get(0).getBarcode());
        assertEquals("testbarcode2", products.get(1).getBarcode());
    }
    @Test
    public void testSelectByBrand() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand,"testproduct","testbarcode",100);
        productService.add(product);
        List<ProductPojo> products  = productService.selectByBrand("testbrand");
        assertEquals(1, products.size());
        assertEquals("testbarcode", products.get(0).getBarcode());
        assertEquals("testproduct", products.get(0).getName());
        assertEquals(100.0, products.get(0).getMrp(), 0.0);
        assertEquals("testbrand", products.get(0).getBrandName());
        assertEquals("testcategory", products.get(0).getCategory());
    }

    @Test
    public void testSelectById() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode",100);
        productService.add(product);
        ProductPojo product2 = productService.selectById(product.getId());
        assertEquals("testbarcode", product2.getBarcode());
        assertEquals("testproduct", product2.getName());
        assertEquals(100.0, product2.getMrp(), 0.0);
        assertEquals("testbrand", product2.getBrandName());
        assertEquals("testcategory", product2.getCategory());
    }

    @Test
    public void testSelectByBrandAndCategory() throws ApiException{
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode",100);
        productService.add(product);
        List<ProductPojo> products = productService.selectByBrandAndCategory("testbrand", "testcategory");
        assertEquals(1, products.size());
        assertEquals("testbarcode", products.get(0).getBarcode());
        assertEquals("testproduct", products.get(0).getName());
        assertEquals(100.0, products.get(0).getMrp(), 0.0);
        assertEquals("testbrand", products.get(0).getBrandName());
        assertEquals("testcategory", products.get(0).getCategory());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brand = createBrand("testbrand", "testcategory");
        brandService.add(brand);
        ProductPojo product = createProduct(brand, "testproduct", "testbarcode",100);
        productService.add(product);
        ProductPojo product2 = new ProductPojo();
        product2.setId(product.getId());
        BrandPojo brand2 = createBrand("testbrand2", "testcategory2");
        brandService.add(brand2);
        product2.setBrandName("testbrand2");
        product2.setCategory("testcategory2");
        product2.setMrp(200);
        product2.setName("testproduct2");
        product2.setBarcode("testbarcode2");
        ProductPojo p2 = productService.selectById(product.getId());
        productService.update(product2,p2.getId());
        ProductPojo product3 = productService.selectById(product.getId());
        assertEquals(200.0, product3.getMrp(), 0.0);
    }
}

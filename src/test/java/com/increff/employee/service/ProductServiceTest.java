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

    public BrandPojo addBrand(String brandName, String category) throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand(brandName);
        p.setCategory(category);
        brandService.add(p);
        return p;
    }

    public ProductPojo addProduct(BrandPojo brand, String name, String barcode, double mrp) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        productService.add(p);
        return p;
    }

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brand = addBrand("testbrand", "testcategory");
        addProduct(brand,"testproduct","testbarcode",100);
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
        BrandPojo brand = addBrand("testBrand", "testCategory");
        ProductPojo product = addProduct(brand,"testproduct1","testbarcode1",100);
        ProductPojo product2 = addProduct(brand,"testproduct2","testbarcode2",200);
        List<ProductPojo> products = productService.selectAll();
        assertEquals(2, products.size());
        assertEquals("testbarcode1", products.get(0).getBarcode());
        assertEquals("testbarcode2", products.get(1).getBarcode());
    }
    @Test
    public void testSelectByBrand() throws ApiException {
        BrandPojo brand = addBrand("testbrand", "testcategory");
        ProductPojo product = addProduct(brand,"testproduct","testbarcode",100);
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
        BrandPojo brand = addBrand("testbrand", "testcategory");
        ProductPojo product = addProduct(brand, "testproduct", "testbarcode",100);
        ProductPojo product2 = productService.selectById(product.getId());
        assertEquals("testbarcode", product2.getBarcode());
        assertEquals("testproduct", product2.getName());
        assertEquals(100.0, product2.getMrp(), 0.0);
        assertEquals("testbrand", product2.getBrandName());
        assertEquals("testcategory", product2.getCategory());
    }

    @Test
    public void testSelectByBrandAndCategory() throws ApiException{
        BrandPojo brand = addBrand("testbrand", "testcategory");
        ProductPojo product = addProduct(brand, "testproduct", "testbarcode",100);
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
        BrandPojo brand = addBrand("testBrand", "testCategory");
        ProductPojo product = addProduct(brand, "testproduct", "testbarcode",100);
        ProductPojo product2 = new ProductPojo();
        product2.setId(product.getId());
        BrandPojo brand2 = addBrand("testbrand2", "testcategory2");
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

package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

public class ProductServiceTest extends AbstractUnitTest{

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ProductDao productDao;

    public BrandPojo createBrandPojo(String brandName, String category) throws ApiException {
        BrandPojo brand = createBrand(brandName, category);
        brandDao.insert(brand);
        BrandPojo brandPojo = brandDao.select(brandName.toLowerCase(),category.toLowerCase());
        return brandPojo;
    }
    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productService.add(product);
        List<ProductPojo> products = productDao.selectAll();
        System.out.println(products.get(0).getBrandName());
        assertEquals(1, products.size());
        assertEquals("testbarcode", products.get(0).getBarcode());
        assertEquals("testproduct", products.get(0).getName());
        assertEquals(100.0, products.get(0).getMrp(), 0.0);
        assertEquals("testbrand", products.get(0).getBrandName());
        assertEquals("testcategory", products.get(0).getCategory());
    }

    @Test
    public void testSelectAll() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brandPojo,"testproduct2","testbarcode2",200.0);
        productDao.insert(product2);
        List<ProductPojo> products = productService.selectAll();
        assertEquals(2, products.size());
        assertEquals("testbarcode", products.get(0).getBarcode());
        assertEquals("testbarcode2", products.get(1).getBarcode());
    }
    @Test
    public void testSelectByBrand() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
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
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = productService.selectById(product.getId());
        assertEquals("testbarcode", product2.getBarcode());
        assertEquals("testproduct", product2.getName());
        assertEquals(100.0, product2.getMrp(), 0.0);
        assertEquals("testbrand", product2.getBrandName());
        assertEquals("testcategory", product2.getCategory());
        try{
            product2 = productService.selectById(100);
            fail();
        }catch(ApiException e){
            assertEquals("The product with id "+ 100 +" does not exists", e.getMessage());
        }

    }

    @Test
    public void testSelectByBrandAndCategory() throws ApiException{
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
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
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = new ProductPojo();
        product2.setId(product.getId());
        BrandPojo brand2 = createBrand("testbrand2", "testcategory2");
        brandDao.insert(brand2);
        product2.setBrandName("testbrand2");
        product2.setCategory("testcategory2");
        product2.setMrp(200.0);
        product2.setName("testproduct2");
        product2.setBarcode("testbarcode2");
        ProductPojo p2 = productDao.selectId(product.getId());
        productService.update(product2,p2.getId());
        ProductPojo product3 = productDao.selectId(product.getId());
        assertEquals(200.0, product3.getMrp(), 0.0);
    }

    @Test
    public void testAddDuplicate() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productService.add(product);
        ProductPojo product2 = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        try {
            productService.add(product2);
            fail();
        } catch (ApiException e) {
            assertEquals("The product already exists", e.getMessage());
        }
    }

    @Test
    public void testUpdateDuplicate() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = createProduct(brandPojo, "testproduct2", "testbarcode2",200.0);
        productDao.insert(product2);
        ProductPojo product3 = new ProductPojo();
        product3.setId(product.getId());
        product3.setBrandName("testbrand");
        product3.setCategory("testcategory");
        product3.setMrp(200.0);
        product3.setName("testproduct2");
        product3.setBarcode("testbarcode2");
        try{
            productService.update(product3, product.getId());
            fail();
        }catch(ApiException e){
            assertEquals("A Product already exists with the same barcode!", e.getMessage());
        }
    }

    @Test
    public void testIsEmpty() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "", "testbarcode",100.0);
        try{
            productService.add(product);
            fail();
        }catch(ApiException e){
            assertEquals("Please fill all the fields!", e.getMessage());
        }
    }

    @Test
    public void testIsNegative() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",-100.0);
        try{
            productService.add(product);
            fail();
        }catch(ApiException e){
            assertEquals("Enter a Valid MRP", e.getMessage());
        }
    }

    @Test
    public void testSelectBarcode() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("testbrand", "testcategory");
        ProductPojo product = createProduct(brandPojo, "testproduct", "testbarcode",100.0);
        productDao.insert(product);
        ProductPojo product2 = productService.selectByBarcode("testbarcode");
        assertEquals("testbarcode", product2.getBarcode());
        assertEquals("testproduct", product2.getName());
        assertEquals(100.0, product2.getMrp(), 0.0);
        assertEquals("testbrand", product2.getBrandName());
        assertEquals("testcategory", product2.getCategory());
        try{
            productService.selectByBarcode("testbarcode2");
            fail();
        }
        catch(ApiException e){
            assertEquals("The product with barcode testbarcode2 does not exists", e.getMessage());
        }
    }

}



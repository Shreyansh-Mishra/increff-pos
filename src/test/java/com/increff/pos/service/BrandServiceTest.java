package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest{
    @Autowired
    private BrandService service;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testBrand");
        p.setCategory("testCategory");
        service.add(p);
        List<BrandPojo> brand = service.selectAll();
        assertEquals(1,brand.size());
        assertEquals("testBrand",brand.get(0).getBrand());
        assertEquals("testCategory",brand.get(0).getCategory());
    }

    @Test
    public void testSelectAll() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testBrand");
        p.setCategory("testCategory");
        service.add(p);
        BrandPojo p2 = new BrandPojo();
        p2.setBrand("testBrand2");
        p2.setCategory("testCategory2");
        service.add(p2);
        List<BrandPojo> brand= service.selectAll();
        assertEquals(2,brand.size());
        assertEquals("testBrand",brand.get(0).getBrand());
        assertEquals("testCategory",brand.get(0).getCategory());
        assertEquals("testBrand2",brand.get(1).getBrand());
        assertEquals("testCategory2",brand.get(1).getCategory());
    }

    @Test
    public void testSelectByNameAndCategory() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testBrand");
        p.setCategory("testCategory");
        service.add(p);
        BrandPojo p2 = new BrandPojo();
        p2.setBrand("testBrand2");
        p2.setCategory("testCategory2");
        service.add(p2);
        BrandPojo brand= service.selectByNameAndCategory("testBrand","testCategory");
        assertEquals("testBrand",brand.getBrand());
        assertEquals("testCategory",brand.getCategory());
        brand = service.selectByNameAndCategory("testBrand2","testCategory2");
        assertEquals("testBrand2",brand.getBrand());
        assertEquals("testCategory2",brand.getCategory());
    }

    @Test
    public void testSelectById() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testBrand");
        brand.setCategory("testCategory");
        service.add(brand);
        BrandPojo brand2 = service.selectById(brand.getId());
        assertEquals(brand.getId(),brand2.getId());
        assertEquals(brand.getBrand(),brand2.getBrand());
        assertEquals(brand.getCategory(),brand2.getCategory());
    }

    @Test
    public void testUpdateBrand() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testBrand");
        brand.setCategory("testCategory");
        service.add(brand);
        BrandPojo b2 = new BrandPojo();
        b2.setBrand("testBrand2");
        b2.setCategory("testCategory2");
        service.updateBrand(brand.getId(),b2);
        BrandPojo brand2 = service.selectById(brand.getId());
        assertEquals(brand.getId(),brand2.getId());
        assertEquals(b2.getBrand(),brand2.getBrand());
        assertEquals(b2.getCategory(),brand2.getCategory());
    }

    @Test
    public void testCheckIfExists() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testBrand");
        brand.setCategory("testCategory");
        service.add(brand);
        BrandPojo b2 = service.checkIfExists(brand.getId());
        assertEquals(brand.getId(),b2.getId());
        assertEquals(brand.getBrand(),b2.getBrand());
        assertEquals(brand.getCategory(),b2.getCategory());
    }

    @Test
    public void testGetCategories() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testBrand");
        brand.setCategory("testCategory");
        service.add(brand);
        BrandPojo brand2 = new BrandPojo();
        brand2.setBrand("testBrand");
        brand2.setCategory("testCategory2");
        service.add(brand2);
        List<String> categories = service.getCategories(brand.getBrand());
        assertEquals(2,categories.size());
        assertEquals("testCategory",categories.get(0));
        assertEquals("testCategory2",categories.get(1));
    }
}

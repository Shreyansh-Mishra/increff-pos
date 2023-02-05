package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrandServiceTest extends AbstractUnitTest{
    @Autowired
    private BrandService service;

    @Autowired
    private BrandDao brandDao;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testBrand");
        p.setCategory("testCategory");
        service.add(p);
        List<BrandPojo> brand = brandDao.selectAll();
        assertEquals(1,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
    }

    @Test
    public void testSelectAll() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testbrand");
        p.setCategory("testcategory");
        brandDao.insert(p);
        BrandPojo p2 = new BrandPojo();
        p2.setBrand("testbrand2");
        p2.setCategory("testcategory2");
        brandDao.insert(p2);
        List<BrandPojo> brand= service.selectAll();
        assertEquals(2,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
        assertEquals("testbrand2",brand.get(1).getBrand());
        assertEquals("testcategory2",brand.get(1).getCategory());
    }

    @Test
    public void testSelectByNameAndCategory() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("testbrand");
        p.setCategory("testcategory");
        brandDao.insert(p);
        BrandPojo p2 = new BrandPojo();
        p2.setBrand("testbrand2");
        p2.setCategory("testcategory2");
        brandDao.insert(p2);
        BrandPojo brand= service.selectByNameAndCategory("testbrand","testcategory");
        assertEquals("testbrand",brand.getBrand());
        assertEquals("testcategory",brand.getCategory());
        brand = service.selectByNameAndCategory("testbrand2","testcategory2");
        assertEquals("testbrand2",brand.getBrand());
        assertEquals("testcategory2",brand.getCategory());
        try{
            service.selectByNameAndCategory("testbrand2","testcategory");
        } catch (ApiException e) {
            assertEquals("The requested brand and category combination does not exists",e.getMessage());
        }
    }

    @Test
    public void testSelectById() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        brandDao.insert(brand);
        BrandPojo brand2 = service.selectById(brand.getId());
        assertEquals(brand.getId(),brand2.getId());
        assertEquals(brand.getBrand().toLowerCase(),brand2.getBrand());
        assertEquals(brand.getCategory().toLowerCase(),brand2.getCategory());
    }

    @Test
    public void testUpdateBrand() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        brandDao.insert(brand);
        BrandPojo b2 = new BrandPojo();
        b2.setBrand("testbrand2");
        b2.setCategory("testcategory2");
        service.updateBrand(brand.getId(),b2);
        BrandPojo brand2 = brandDao.select(brand.getId());
        assertEquals(brand.getId(),brand2.getId());
        assertEquals(b2.getBrand().toLowerCase(),brand2.getBrand());
        assertEquals(b2.getCategory().toLowerCase(),brand2.getCategory());
    }

    @Test
    public void testCheckIfExists() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        brandDao.insert(brand);
        BrandPojo b2 = service.checkIfExists(brand.getId());
        assertEquals(brand.getId(),b2.getId());
        assertEquals(brand.getBrand().toLowerCase(),b2.getBrand());
        assertEquals(brand.getCategory().toLowerCase(),b2.getCategory());
        try{
            service.checkIfExists(brand.getId()+1);
        } catch (ApiException e) {
            assertEquals("The requested brand does not exists",e.getMessage());
        }
    }

    @Test
    public void testGetCategories() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        brandDao.insert(brand);
        BrandPojo brand2 = new BrandPojo();
        brand2.setBrand("testbrand");
        brand2.setCategory("testcategory2");
        brandDao.insert(brand2);
        List<String> categories = service.getCategories(brand.getBrand());
        assertEquals(2,categories.size());
        assertEquals("testcategory",categories.get(0));
        assertEquals("testcategory2",categories.get(1));
    }

    @Test
    public void testAddDuplicate() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        service.add(brand);
        BrandPojo brand2 = new BrandPojo();
        brand2.setBrand("testbrand");
        brand2.setCategory("testcategory");
        try {
            service.add(brand2);
            fail();
        } catch (ApiException e) {
            assertEquals("Brand and Category Already Exists",e.getMessage());
        }
    }

    @Test
    public void testIsEmpty() {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("");
        try{
            service.add(brand);
            fail();
        } catch (ApiException e) {
            assertEquals("Brand or Category cannot be empty!",e.getMessage());
        }
    }

    @Test
    public void testUpdateDuplicate() throws ApiException {
        BrandPojo brand = new BrandPojo();
        brand.setBrand("testbrand");
        brand.setCategory("testcategory");
        brandDao.insert(brand);
        BrandPojo brand2 = new BrandPojo();
        brand2.setBrand("testbrand2");
        brand2.setCategory("testcategory2");
        brandDao.insert(brand2);
        BrandPojo brand3 = new BrandPojo();
        brand3.setBrand("testbrand2");
        brand3.setCategory("testcategory2");
        try{
            service.updateBrand(brand.getId(),brand3);
            fail();
        } catch (ApiException e) {
            assertEquals("The Brand and Category already exists!",e.getMessage());
        }
    }
}

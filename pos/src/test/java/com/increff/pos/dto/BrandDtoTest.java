package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandDao brandDao;
    @Test
    public void testCreateBrand() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        brandDto.createBrand(form);
        List<BrandPojo> brand = brandDao.selectAll();
        assertEquals(1,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
    }

    @Test
    public void testGetAllBrands() throws ApiException {
        BrandPojo brandPojo = createBrand("testbrand", "testcategory");
        brandDao.insert(brandPojo);
        BrandPojo brandPojo2 = createBrand("testbrand2", "testcategory2");
        brandDao.insert(brandPojo2);
        List<BrandData> brand= brandDto.getAllBrands();
        assertEquals(2,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
        assertEquals("testbrand2",brand.get(1).getBrand());
        assertEquals("testcategory2",brand.get(1).getCategory());
    }

    @Test
    public void testGetBrandByNameAndCategory() throws ApiException {
        BrandPojo brandPojo = createBrand("testbrand", "testcategory");
        brandDao.insert(brandPojo);
        BrandPojo brandPojo2 = createBrand("testbrand2", "testcategory2");
        brandDao.insert(brandPojo2);
        BrandData brand= brandDto.getBrandByNameAndCategory("testBrand","testCategory");
        assertEquals("testbrand",brand.getBrand());
        assertEquals("testcategory",brand.getCategory());
        brand = brandDto.getBrandByNameAndCategory("testBrand2","testCategory2");
        assertEquals("testbrand2",brand.getBrand());
        assertEquals("testcategory2",brand.getCategory());
    }

    @Test
    public void testGetBrandById() throws ApiException {
        BrandPojo brandPojo = createBrand("testbrand", "testcategory");
        brandDao.insert(brandPojo);
        List<BrandPojo> b = brandDao.selectAll();
        BrandData brand = brandDto.getBrandById(b.get(0).getId());
        assertEquals("testbrand",brand.getBrand());
        assertEquals("testcategory",brand.getCategory());
    }

    @Test
    public void testUpdateBrand() throws ApiException{
        BrandPojo brandPojo = createBrand("testbrand", "testcategory");
        brandDao.insert(brandPojo);
        List<BrandPojo> b = brandDao.selectAll();
        BrandForm form2 = new BrandForm();
        form2.setBrand("testBrand2");
        form2.setCategory("testCategory2");
        brandDto.updateBrand(b.get(0).getId(),form2);
        BrandPojo brand = brandDao.select(b.get(0).getId());
        assertEquals("testbrand2",brand.getBrand());
        assertEquals("testcategory2",brand.getCategory());
    }

    @Test
    public void testGetCategoriesByBrand() throws ApiException {
        BrandPojo brandPojo = createBrand("testbrand", "testcategory");
        brandDao.insert(brandPojo);
        BrandPojo brandPojo2 = createBrand("testbrand", "testcategory2");
        brandDao.insert(brandPojo2);
        List<String> categories = brandDto.getCategoriesByBrand("testbrand");
        assertEquals(2,categories.size());
        assertEquals("testcategory",categories.get(0));
        assertEquals("testcategory2",categories.get(1));
    }
}

package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class BrandFlowTest extends AbstractUnitTest {
    @Autowired
    private BrandFlow flow;

    @Test
    public void testCreateBrand() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        List<BrandData> brand = flow.getAllBrands();
        assertEquals(1,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
    }

    @Test
    public void testGetAllBrands() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        BrandForm form2 = new BrandForm();
        form2.setBrand("testBrand2");
        form2.setCategory("testCategory2");
        flow.createBrand(form2);
        List<BrandData> brand= flow.getAllBrands();
        assertEquals(2,brand.size());
        assertEquals("testbrand",brand.get(0).getBrand());
        assertEquals("testcategory",brand.get(0).getCategory());
        assertEquals("testbrand2",brand.get(1).getBrand());
        assertEquals("testcategory2",brand.get(1).getCategory());
    }

    @Test
    public void testGetBrandByNameAndCategory() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        BrandForm form2 = new BrandForm();
        form2.setBrand("testBrand2");
        form2.setCategory("testCategory2");
        flow.createBrand(form2);
        BrandData brand= flow.getBrandByNameAndCategory("testBrand","testCategory");
        assertEquals("testbrand",brand.getBrand());
        assertEquals("testcategory",brand.getCategory());
        brand = flow.getBrandByNameAndCategory("testBrand2","testCategory2");
        assertEquals("testbrand2",brand.getBrand());
        assertEquals("testcategory2",brand.getCategory());
    }

    @Test
    public void testGetBrandById() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        List<BrandData> b = flow.getAllBrands();
        BrandData brand = flow.getBrandById(b.get(0).getId());
        assertEquals("testbrand",brand.getBrand());
        assertEquals("testcategory",brand.getCategory());
    }

    @Test
    public void testUpdateBrand() throws ApiException{
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        List<BrandData> b = flow.getAllBrands();
        BrandForm form2 = new BrandForm();
        form2.setBrand("testBrand2");
        form2.setCategory("testCategory2");
        flow.updateBrand(b.get(0).getId(),form2);
        BrandData brand = flow.getBrandById(b.get(0).getId());
        assertEquals("testbrand2",brand.getBrand());
        assertEquals("testcategory2",brand.getCategory());
    }

    @Test
    public void testGetCategoriesByBrand() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("testBrand");
        form.setCategory("testCategory");
        flow.createBrand(form);
        BrandForm form2 = new BrandForm();
        form2.setBrand("testBrand");
        form2.setCategory("testCategory2");
        flow.createBrand(form2);
        List<String> categories = flow.getCategoriesByBrand("testbrand");
        assertEquals(2,categories.size());
        assertEquals("testcategory",categories.get(0));
        assertEquals("testcategory2",categories.get(1));
    }
}

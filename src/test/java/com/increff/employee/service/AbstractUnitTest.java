package com.increff.employee.service;

import javax.transaction.Transactional;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {
    public BrandPojo createBrand(String brandName, String category) throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand(brandName);
        p.setCategory(category);
        return p;
    }

    public ProductPojo createProduct(BrandPojo brand, String name, String barcode, double mrp) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setBrandName(brand.getBrand());
        p.setCategory(brand.getCategory());
        p.setMrp(mrp);
        p.setName(name);
        p.setBarcode(barcode);
        return p;
    }

    public InventoryPojo createInventory(ProductPojo product, int quantity){
        InventoryPojo i = new InventoryPojo();
        i.setBarcode(product.getBarcode());
        i.setQuantity(quantity);
        return i;
    }
}

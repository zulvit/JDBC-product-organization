package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zulvit.entity.Product;
import ru.zulvit.flyway.FlywayInitializer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoImplTest {
    private final ProductDaoImpl productDao = new ProductDaoImpl();

    @BeforeEach
    void init() {
        FlywayInitializer.initDb();
    }

    @Test
    public void testFindById() {
        Product product = new Product(Integer.MAX_VALUE, "test");
        productDao.save(product);
        Optional<Product> byId = productDao.findById(Integer.MAX_VALUE);
        byId.ifPresent(value -> assertEquals(product, value));
    }

    @Test
    public void getAll() {
        Product product = new Product(Integer.MAX_VALUE, "test");
        productDao.save(product);
        List<@NotNull Product> listAllProduct = productDao.getAll();
        boolean flag = false;
        for (Product value : listAllProduct) {
            if (value.equals(product)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testAddInvoice() {
        Product product = new Product(Integer.MAX_VALUE, "test");
        productDao.save(product);
        if (productDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(productDao.findById(Integer.MAX_VALUE).get(), product);
        }
    }

    @Test
    public void testDeleteInvoice() {
        Product product = new Product(Integer.MAX_VALUE, "test");
        productDao.save(product);
        productDao.delete(product);
        List<@NotNull Product> productList = productDao.getAll();
        for (Product value : productList) {
            assertNotEquals(value, product);
        }
    }

    @Test
    public void testUpdateInvoice() {
        Product product = new Product(Integer.MAX_VALUE, "test");
        Product product1 = new Product(Integer.MAX_VALUE, "test1");
        productDao.save(product1);
        productDao.update(product);
        if (productDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(productDao.findById(Integer.MAX_VALUE).get(), product);
        }
    }
}
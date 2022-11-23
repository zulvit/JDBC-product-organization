package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zulvit.entity.Invoice;
import ru.zulvit.flyway.FlywayInitializer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceDaoImplTest {
    private final InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

    @BeforeEach
    void init() {
        FlywayInitializer.initDb();
    }

    @Test
    public void testFindById() {
        Invoice invoice = new Invoice(Integer.MAX_VALUE, 1, 1, 333f, 3);
        invoiceDao.save(invoice);
        Optional<Invoice> byId = invoiceDao.findById(Integer.MAX_VALUE);
        byId.ifPresent(value -> assertEquals(invoice, value));
    }

    @Test
    public void getAll() {
        Invoice invoice = new Invoice(Integer.MAX_VALUE, 1, 1, 333f, 3);
        invoiceDao.save(invoice);
        List<@NotNull Invoice> listAllInvoice = invoiceDao.getAll();
        boolean flag = false;
        for (Invoice value : listAllInvoice) {
            if (value.equals(invoice)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testAddInvoice() {
        Invoice invoice = new Invoice(Integer.MAX_VALUE, 1, 1, 333f, 3);
        invoiceDao.save(invoice);
        if (invoiceDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(invoiceDao.findById(Integer.MAX_VALUE).get(), invoice);
        }
    }

    @Test
    public void testDeleteInvoice() {
        Invoice invoice = new Invoice(Integer.MAX_VALUE, 1, 1, 333f, 3);
        invoiceDao.save(invoice);
        invoiceDao.delete(invoice);
        List<@NotNull Invoice> invoiceList = invoiceDao.getAll();
        for (Invoice value : invoiceList) {
            assertNotEquals(value, invoice);
        }
    }

    @Test
    public void testUpdateInvoice() {
        Invoice invoice = new Invoice(Integer.MAX_VALUE, 1, 1, 333f, 3);
        Invoice invoice1 = new Invoice(Integer.MAX_VALUE, 2, 2, 555f, 10);
        invoiceDao.save(invoice1);
        invoiceDao.update(invoice);
        if (invoiceDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(invoiceDao.findById(Integer.MAX_VALUE).get(), invoice);
        }
    }
}
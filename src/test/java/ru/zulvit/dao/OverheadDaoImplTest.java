package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zulvit.entity.Overhead;
import ru.zulvit.flyway.FlywayInitializer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OverheadDaoImplTest {
    private final OverheadDaoImpl overheadDao = new OverheadDaoImpl();

    @BeforeEach
    void init() {
        FlywayInitializer.initDb();
    }

    @Test
    public void testFindById() {
        Overhead overhead = new Overhead(Integer.MAX_VALUE, "2001-01-01", 827364);
        overheadDao.save(overhead);
        Optional<Overhead> byId = overheadDao.findById(Integer.MAX_VALUE);
        byId.ifPresent(value -> assertEquals(overhead, value));
    }

    @Test
    public void getAll() {
        Overhead overhead = new Overhead(Integer.MAX_VALUE, "2001-01-01", 827364);
        overheadDao.save(overhead);
        List<@NotNull Overhead> listAllOrganization = overheadDao.getAll();
        boolean flag = false;
        for (Overhead value : listAllOrganization) {
            if (value.equals(overhead)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testAddInvoice() {
        Overhead overhead = new Overhead(Integer.MAX_VALUE, "2001-01-01", 827364);
        overheadDao.save(overhead);
        if (overheadDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(overheadDao.findById(Integer.MAX_VALUE).get(), overhead);
        }
    }

    @Test
    public void testDeleteInvoice() {
        Overhead overhead = new Overhead(Integer.MAX_VALUE, "2001-01-01", 827364);
        overheadDao.save(overhead);
        overheadDao.delete(overhead);
        List<@NotNull Overhead> organizationList = overheadDao.getAll();
        for (Overhead value : organizationList) {
            assertNotEquals(value, overhead);
        }
    }

    @Test
    public void testUpdateInvoice() {
        Overhead overhead = new Overhead(Integer.MAX_VALUE, "2001-01-01", 827364);
        Overhead overhead1 = new Overhead(Integer.MAX_VALUE, "2011-11-11", 827364);
        overheadDao.save(overhead1);
        overheadDao.update(overhead);
        if (overheadDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(overheadDao.findById(Integer.MAX_VALUE).get(), overhead);
        }
    }
}
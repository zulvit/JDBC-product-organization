package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zulvit.entity.Organization;
import ru.zulvit.flyway.FlywayInitializer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationDaoImplTest {
    private final OrganizationDaoImpl organizationDao = new OrganizationDaoImpl();

    @BeforeEach
    void init() {
        FlywayInitializer.initDb();
    }

    @Test
    public void testFindById() {
        Organization organization = new Organization(Integer.MAX_VALUE, "test", 1);
        organizationDao.save(organization);
        Optional<Organization> byId = organizationDao.findById(Integer.MAX_VALUE);
        byId.ifPresent(value -> assertEquals(organization, value));
    }

    @Test
    public void getAll() {
        Organization organization = new Organization(Integer.MAX_VALUE, "test", 1);
        organizationDao.save(organization);
        List<@NotNull Organization> listAllOrganization = organizationDao.getAll();
        boolean flag = false;
        for (Organization value : listAllOrganization) {
            if (value.equals(organization)) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void testAddInvoice() {
        Organization organization = new Organization(Integer.MAX_VALUE, "test", 1);
        organizationDao.save(organization);
        if (organizationDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(organizationDao.findById(Integer.MAX_VALUE).get(), organization);
        }
    }

    @Test
    public void testDeleteInvoice() {
        Organization organization = new Organization(Integer.MAX_VALUE, "test", 1);
        organizationDao.save(organization);
        organizationDao.delete(organization);
        List<@NotNull Organization> organizationList = organizationDao.getAll();
        for (Organization value : organizationList) {
            assertNotEquals(value, organization);
        }
    }

    @Test
    public void testUpdateInvoice() {
        Organization organization = new Organization(Integer.MAX_VALUE, "test", 1);
        Organization organization1 = new Organization(Integer.MAX_VALUE, "test1", 2);
        organizationDao.save(organization1);
        organizationDao.update(organization);
        if (organizationDao.findById(Integer.MAX_VALUE).isPresent()) {
            assertEquals(organizationDao.findById(Integer.MAX_VALUE).get(), organization);
        }
    }
}
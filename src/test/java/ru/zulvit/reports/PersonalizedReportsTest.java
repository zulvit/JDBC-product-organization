package ru.zulvit.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zulvit.flyway.FlywayInitializer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonalizedReportsTest {
    private final PersonalizedReports personalizedReports = new PersonalizedReports();

    @BeforeEach
    void init() {
        FlywayInitializer.initDb();
    }

    @Test
    void topNByDelivered() {
        Map<Integer, Integer> integerIntegerMap = personalizedReports.topNByDelivered(1);
        assertNotNull(integerIntegerMap);
        assertEquals(1, integerIntegerMap.size());
    }

    @Test
    void supplierOfAtLeast() {
        final int minValue = 1;
        Map<Integer, PersonalizedReports.CustomSupplier> integerCustomSupplierMap = personalizedReports.supplierOfAtLeast(minValue);
        assertNotNull(integerCustomSupplierMap);
        boolean flag = false;

        for (Integer key : integerCustomSupplierMap.keySet()) {
            if (integerCustomSupplierMap.get(key).amount() > minValue) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    @Test
    void dateMethods() {
        Calendar calendar1 = new GregorianCalendar(2022, Calendar.SEPTEMBER, 8);
        Calendar calendar2 = new GregorianCalendar(2024, Calendar.SEPTEMBER, 9);
        assertNotNull(personalizedReports.calcAllProduct(calendar1, calendar2));
        assertNotNull(personalizedReports.calcAveragePriceOfThePeriod(calendar1, calendar2));
        assertNotNull(personalizedReports.dateProduct(calendar1, calendar2));
    }
}
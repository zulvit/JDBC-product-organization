package ru.zulvit;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.dao.InvoiceDaoImpl;
import ru.zulvit.dao.OrganizationDaoImpl;
import ru.zulvit.dao.OverheadDaoImpl;
import ru.zulvit.dao.ProductDaoImpl;
import ru.zulvit.flyway.FlywayInitializer;
import ru.zulvit.reports.PersonalizedReports;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(@NotNull String[] args) {
        //init database
        FlywayInitializer.initDb();

        //demonstration dao
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
        System.out.println(invoiceDao.getAll());
        OrganizationDaoImpl organizationDao = new OrganizationDaoImpl();
        System.out.println(organizationDao.getAll());
        OverheadDaoImpl overheadDao = new OverheadDaoImpl();
        System.out.println(overheadDao.getAll());
        ProductDaoImpl productDao = new ProductDaoImpl();
        System.out.println(productDao.getAll());

        System.out.println("****************");

        Calendar calendar1 = new GregorianCalendar(2022, Calendar.SEPTEMBER, 8);
        Calendar calendar2 = new GregorianCalendar(2024, Calendar.SEPTEMBER, 9);

        //demonstration custom reports
        System.out.println("Выбрать первые 10 поставщиков по количеству поставленного товара:");
        System.out.println("\t" + new PersonalizedReports().topNByDelivered(10));
        System.out.println("Выбрать поставщиков с количеством поставленного товара выше указанного значения:");
        System.out.println("\t" + new PersonalizedReports().supplierOfAtLeast(1));
        System.out.println("Вывести список товаров, поставленных организациями за период. Если организация товары не поставляла, то она все равно должна быть отражена в списке.");
        System.out.println("\t" + new PersonalizedReports().dateProduct(calendar1, calendar2));
        System.out.println("Рассчитать среднюю цену по каждому товару за период");
        System.out.println("\t" + new PersonalizedReports().calcAveragePriceOfThePeriod(calendar1, calendar2));
        System.out.println("За каждый день для каждого товара рассчитать количество и сумму полученного товара в указанном периоде, посчитать итоги за период");
        System.out.println("\t" + new PersonalizedReports().calcAllProduct(calendar1, calendar2));
    }
}
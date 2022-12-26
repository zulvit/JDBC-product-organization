package ru.zulvit.reports;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.flyway.JDBCCredentials;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalizedReports {
    private final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    record CustomSupplier(int amount, @NotNull List<Integer> listProductsId) {
    }

    record AveragePrice(double price, int amount) {
    }

    private record ListForThePeriod(int amount, @NotNull List<Integer> listProductsId) {
    }

    /**
     * @param n сколько вывести поставщиков
     * @return первые n поставщиков по количеству поставленного товара
     */
    @NotNull
    public Map<Integer, Integer> topNByDelivered(int n) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT overhead.organization_id, sum(invoice.amount) " +
                    "OVER(PARTITION BY overhead.organization_id) AS total_sum " +
                    "FROM overhead JOIN invoice ON overhead.\"ID\" = invoice.\"ID\" " +
                    "ORDER BY total_sum " +
                    "DESC LIMIT " + n);
            Map<Integer, Integer> map = new LinkedHashMap<>();

            while (resultSet.next()) {
                Integer organizationId = resultSet.getInt("organization_id");
                Integer totalSum = resultSet.getInt("total_sum");
                map.put(organizationId, totalSum);
            }

            return map.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param n минимальное значение товара
     * @return поставщики, с количеством поставленного товара выше n
     */
    @NotNull
    public Map<Integer, CustomSupplier> supplierOfAtLeast(int n) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT overhead.organization_id, invoice.amount, invoice.product_id " +
                    "FROM overhead JOIN invoice ON overhead.\"ID\" = invoice.\"ID\"");
            TreeMap<Integer, CustomSupplier> map = new TreeMap<>();

            List<Integer> keysArray = new ArrayList<>();
            List<Integer> listOfProducts;
            while (resultSet.next()) {
                listOfProducts = new ArrayList<>();
                Integer orgId = resultSet.getInt("organization_id");
                keysArray.add(orgId);
                int amount = resultSet.getInt("amount");
                listOfProducts.add(resultSet.getInt("product_id"));
                if (map.get(orgId) != null) {
                    map.put(orgId, new CustomSupplier(amount + map.get(orgId).amount,
                            Stream.concat(map.get(orgId).listProductsId.stream(), listOfProducts.stream())
                                    .collect(Collectors.toList())));
                } else {
                    map.put(orgId, new CustomSupplier(amount, listOfProducts));
                }
            }
            Map<Integer, CustomSupplier> resultMap = new TreeMap<>();
            for (int i = 0; i < map.keySet().size(); i++) {
                int amount = map.get(map.keySet().toArray()[i]).amount; // amount
                int id = (int) map.keySet().toArray()[i];
                if (amount > n) {
                    resultMap.put(id, map.get(map.keySet().toArray()[i]));
                }
            }
            return resultMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet prepareDate(@NotNull Calendar start, @NotNull Calendar end, Statement statement, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, start.get(Calendar.YEAR) + "-" + (start.get(Calendar.MONTH) + 1) + "-" + start.get(Calendar.DAY_OF_MONTH));
        preparedStatement.setString(2, end.get(Calendar.YEAR) + "-" + (end.get(Calendar.MONTH) + 1) + "-" + end.get(Calendar.DAY_OF_MONTH));
        return statement.executeQuery(preparedStatement.toString());
    }

    /**
     * @param start начало периода
     * @param end   конец периода
     * @return количество и сумма полученного товара в указанном периоде
     */
    public Map<String, Map<Integer, Double>> calcAllProduct(@NotNull Calendar start, @NotNull Calendar end) {
        Map<String, Map<Integer, Double>> replyMap = new TreeMap<>(); //Calendar - дата
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT overhead.date, invoice.product_id, invoice.price, invoice.amount " +
                    "FROM overhead JOIN invoice ON overhead.\"ID\" = invoice.\"ID\"" +
                    "WHERE date BETWEEN ? AND ?");
            ResultSet resultSet = prepareDate(start, end, statement, preparedStatement);
            while (resultSet.next()) {
                Map<Integer, Double> idProductMap = new TreeMap<>(); //Integer - id товара, Double - цена
                Integer productId = resultSet.getInt("product_id");
                double price = resultSet.getDouble("price");
                int amount = resultSet.getInt("amount");
                String date = resultSet.getString("date");
                idProductMap.merge(productId, price * amount, Double::sum);
                replyMap.put(date, idProductMap);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return replyMap;
    }

    /**
     * @param start начало периода
     * @param end   конец периода
     * @return средняя цена по каждому товару за период
     */
    public Map<Integer, Double> calcAveragePriceOfThePeriod(@NotNull Calendar start, @NotNull Calendar end) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT invoice.product_id, invoice.price, invoice.amount " +
                    "FROM overhead JOIN invoice ON overhead.\"ID\" = invoice.\"ID\"" +
                    "WHERE date BETWEEN ? AND ?");
            ResultSet resultSet = prepareDate(start, end, statement, preparedStatement);
            Map<Integer, AveragePrice> map = new TreeMap<>();
            while (resultSet.next()) {
                Integer key = resultSet.getInt("product_id");
                int price = resultSet.getInt("price");
                int amount = resultSet.getInt("amount");
                if (map.get(key) != null) {
                    map.put(key, new AveragePrice(map.get(key).price + price, map.get(key).amount + amount));
                } else {
                    map.put(key, new AveragePrice(price, amount));
                }
            }
            Map<Integer, Double> averageMap = new TreeMap<>();
            HashSet<Integer> set = new HashSet<>(map.keySet());
            Object[] arrayKeys = set.toArray();
            for (int i = 0; i < map.size(); i++) {
                double price = map.get((Integer) arrayKeys[i]).price / map.get((Integer) arrayKeys[i]).amount;
                averageMap.put((Integer) arrayKeys[i], price);
            }
            return averageMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param start начало периода
     * @param end   конец периода
     * @return список товаров, поставленных организациями за период.
     */
    @NotNull
    public Map<Integer, ListForThePeriod> dateProduct(@NotNull Calendar start, @NotNull Calendar end) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            var preparedStatement = connection.prepareStatement("SELECT overhead.organization_id, invoice.amount, invoice.product_id " +
                    "FROM overhead JOIN invoice ON overhead.\"ID\" = invoice.\"ID\"" +
                    "WHERE DATE BETWEEN ? AND ?");
            ResultSet resultSet = prepareDate(start, end, statement, preparedStatement);

            TreeMap<Integer, ListForThePeriod> map = new TreeMap<>();
            while (resultSet.next()) {
                List<Integer> list = new ArrayList<>();
                Integer key = resultSet.getInt("organization_id");
                int amount = resultSet.getInt("amount");
                list.add(resultSet.getInt("product_id"));
                if (map.get(key) != null) {
                    map.put(key, new ListForThePeriod(amount + map.get(key).amount, Stream.concat(map.get(key).listProductsId.stream(), list.stream())
                            .collect(Collectors.toList())));
                } else {
                    map.put(key, new ListForThePeriod(amount, list));
                }
            }
            return map;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
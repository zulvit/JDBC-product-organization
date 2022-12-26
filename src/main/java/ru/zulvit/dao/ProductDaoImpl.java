package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.entity.Product;
import ru.zulvit.flyway.JDBCCredentials;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ProductDaoImpl implements DAO<Product> {
    private final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Optional<Product> findById(int id) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Products WHERE \"ID\" = " + id);
            Product product = null;
            while (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("ID"),
                        resultSet.getString("title")
                );
            }
            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public @NotNull List<@NotNull Product> getAll() {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Products");
            List<Product> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Product(
                        resultSet.getInt("ID"),
                        resultSet.getString("title"))
                );
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            var prepareStatement = connection.prepareStatement(
                    "INSERT INTO Products (\"ID\", title) " +
                            "VALUES (?, ?)");
            prepareStatement.setInt(1, entity.id());
            prepareStatement.setString(2, entity.title());
            statement.executeUpdate(prepareStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                var prepareStatement = connection.prepareStatement(
                        "UPDATE Products SET title = ? where \"ID\" = ?");
                prepareStatement.setString(1, entity.title());
                prepareStatement.setInt(2, entity.id());
                statement.executeUpdate(prepareStatement.toString());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                var preparedStatement = connection.prepareStatement(
                        "DELETE FROM Products WHERE \"ID\" = ?");
                preparedStatement.setInt(1, entity.id());
                statement.executeUpdate(preparedStatement.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.entity.Invoice;
import ru.zulvit.flyway.JDBCCredentials;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class InvoiceDaoImpl implements DAO<Invoice> {
    private final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Optional<Invoice> findById(int id) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Invoice WHERE \"ID\" = " + id);
            Invoice invoice = null;
            while (resultSet.next()) {
                invoice = new Invoice(
                        resultSet.getInt("ID"),
                        resultSet.getInt("overhead_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("price"),
                        resultSet.getInt("amount")
                );
            }
            if (invoice != null) {
                return Optional.of(invoice);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public @NotNull List<@NotNull Invoice> getAll() {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Invoice");
            List<Invoice> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Invoice(
                        resultSet.getInt("ID"),
                        resultSet.getInt("overhead_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("price"),
                        resultSet.getInt("amount"))
                );
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                var prepareStatement = connection.prepareStatement(
                        "INSERT INTO Invoice (\"ID\", overhead_id, product_id, price, amount) " +
                                "VALUES (?, ?, ?, ?, ?)");
                prepareStatement.setInt(1, entity.ID());
                prepareStatement.setInt(2, entity.overheadId());
                prepareStatement.setInt(3, entity.productId());
                prepareStatement.setDouble(4, entity.price());
                prepareStatement.setInt(5, entity.amount());
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
    public void update(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                var prepareStatement = connection.prepareStatement(
                        "UPDATE Invoice SET overhead_id = ? where \"ID\" = ?");
                prepareStatement.setInt(1, entity.overheadId());
                prepareStatement.setInt(2, entity.ID());
                statement.executeUpdate(prepareStatement.toString());

                prepareStatement = connection.prepareStatement(
                        "UPDATE Invoice SET product_id = ? where \"ID\" = ?");
                prepareStatement.setInt(1, entity.productId());
                prepareStatement.setInt(2, entity.ID());
                statement.executeUpdate(prepareStatement.toString());

                prepareStatement = connection.prepareStatement(
                        "UPDATE Invoice SET price = ? where \"ID\" = ?");
                prepareStatement.setDouble(1, entity.price());
                prepareStatement.setInt(2, entity.ID());
                statement.executeUpdate(prepareStatement.toString());

                prepareStatement = connection.prepareStatement(
                        "UPDATE Invoice SET amount = ? where \"ID\" = ?");
                prepareStatement.setInt(1, entity.amount());
                prepareStatement.setInt(2, entity.ID());
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
    public void delete(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                var preparedStatement = connection.prepareStatement(
                        "DELETE FROM Invoice WHERE \"ID\" = ?");
                preparedStatement.setInt(1, entity.ID());
                statement.executeUpdate(preparedStatement.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.entity.Overhead;
import ru.zulvit.flyway.JDBCCredentials;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class OverheadDaoImpl implements DAO<Overhead> {
    private final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Optional<Overhead> findById(int id) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Overhead WHERE \"ID\" = " + id);
            Overhead overhead = null;
            while (resultSet.next()) {
                overhead = new Overhead(
                        resultSet.getInt("ID"),
                        resultSet.getString("date"),
                        resultSet.getInt("organization_id")
                );
            }
            if (overhead != null) {
                return Optional.of(overhead);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public @NotNull List<@NotNull Overhead> getAll() {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Overhead");
            List<Overhead> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Overhead(
                        resultSet.getInt("ID"),
                        resultSet.getString("date"),
                        resultSet.getInt("organization_id"))
                );
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(@NotNull Overhead entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            var prepareStatement = connection.prepareStatement(
                    "INSERT INTO Overhead (\"ID\", date, organization_id) " +
                            "VALUES (?, ?, ?)");
            prepareStatement.setInt(1, entity.id());
            prepareStatement.setString(2, entity.date());
            prepareStatement.setInt(3, entity.organizationId());
            statement.executeUpdate(prepareStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Overhead entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                var prepareStatement = connection.prepareStatement(
                        "UPDATE Overhead SET date = ? where \"ID\" = ?");
                prepareStatement.setString(1, entity.date());
                prepareStatement.setInt(2, entity.id());
                statement.executeUpdate(prepareStatement.toString());

                prepareStatement = connection.prepareStatement(
                        "UPDATE Overhead SET organization_id = ? where \"ID\" = ?");
                prepareStatement.setInt(1, entity.organizationId());
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
    public void delete(@NotNull Overhead entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                var preparedStatement = connection.prepareStatement(
                        "DELETE FROM Overhead WHERE \"ID\" = ?");
                preparedStatement.setInt(1, entity.id());
                statement.executeUpdate(preparedStatement.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
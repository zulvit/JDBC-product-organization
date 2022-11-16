package ru.zulvit.dao;

import org.jetbrains.annotations.NotNull;
import ru.zulvit.entity.Organization;
import ru.zulvit.flyway.JDBCCredentials;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class OrganizationDaoImpl implements DAO<Organization> {
    private final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Optional<Organization> findById(int inn) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Organizations WHERE \"INN\" = " + inn);
            Organization organization = null;
            while (resultSet.next()) {
                organization = new Organization(
                        resultSet.getInt("INN"),
                        resultSet.getString("name"),
                        resultSet.getInt("checking_account")
                );
            }
            if (organization != null) {
                return Optional.of(organization);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public @NotNull List<@NotNull Organization> getAll() {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Organizations");
            List<Organization> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Organization(
                        resultSet.getInt("INN"),
                        resultSet.getString("name"),
                        resultSet.getInt("checking_account"))
                );
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            Statement statement = connection.createStatement();
            var prepareStatement = connection.prepareStatement(
                    "INSERT INTO Organizations (\"INN\", name, checking_account) " +
                            "VALUES (?, ?, ?)");
            prepareStatement.setInt(1, entity.inn());
            prepareStatement.setString(2, entity.name());
            prepareStatement.setInt(3, entity.checking_account());
            statement.executeUpdate(prepareStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);
                var prepareStatement = connection.prepareStatement(
                        "UPDATE Organizations SET name = ? where \"INN\" = ?");
                prepareStatement.setString(1, entity.name());
                prepareStatement.setInt(2, entity.inn());
                statement.executeUpdate(prepareStatement.toString());

                prepareStatement = connection.prepareStatement(
                        "UPDATE Organizations SET checking_account = ? where \"INN\" = ?");
                prepareStatement.setInt(1, entity.checking_account());
                prepareStatement.setInt(2, entity.inn());
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
    public void delete(@NotNull Organization entity) {
        try (Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            try (Statement statement = connection.createStatement()) {
                var preparedStatement = connection.prepareStatement(
                        "DELETE FROM Organizations WHERE \"INN\" = ?");
                preparedStatement.setInt(1, entity.inn());
                statement.executeUpdate(preparedStatement.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

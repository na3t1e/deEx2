package ru.nasti.demoexam.manager;

import ru.nasti.Main;
import ru.nasti.demoexam.entity.ClientEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientEntityManager {
    public static void insert(ClientEntity clientEntity) throws SQLException {
        try (Connection c = Main.getConnection()) {
            String sql = "Insert into Client(FirstName, LastName, Patronymic, Birthday, RegistrationDate, Email, " +
                    "Phone, GenderCode, PhotoPath) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            p.setString(1, clientEntity.getFirstName());
            p.setString(2, clientEntity.getLastName());
            p.setString(3, clientEntity.getPatronymic());
            p.setTimestamp(4, new Timestamp(clientEntity.getBirthday().getTime()));
            p.setTimestamp(5, new Timestamp(clientEntity.getRegistrationDate().getTime()));
            p.setString(6, clientEntity.getEmail());
            p.setString(7, clientEntity.getPhone());
            p.setString(8, clientEntity.getGenderCode());
            p.setString(9, clientEntity.getPhotoPath());

            p.executeUpdate();

            ResultSet keys = p.getGeneratedKeys();

            if (keys.next()) {
                clientEntity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("Клиен не добавлен!");
        }
    }

    public static List<ClientEntity> select() throws SQLException {
        try (Connection c = Main.getConnection()) {
            String sql = "Select * from Client";

            Statement statement = c.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            List<ClientEntity> clientEntities = new ArrayList<>();

            while (resultSet.next()) {
                clientEntities.add(new ClientEntity(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        resultSet.getTimestamp(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getString(10)
                ));
            }
            return clientEntities;
        }
    }

    public static boolean edit(ClientEntity clientEntity) throws SQLException {
        try (Connection c = Main.getConnection()) {
            String sql = "Update Client set FirstName = ?, LastName = ?, Patronymic = ?, Birthday = ?, RegistrationDate = ?, Email = ?, " +
                    "Phone = ?, GenderCode = ?, PhotoPath = ? where ID = ?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, clientEntity.getFirstName());
            p.setString(2, clientEntity.getLastName());
            p.setString(3, clientEntity.getPatronymic());
            p.setTimestamp(4, new Timestamp(clientEntity.getBirthday().getTime()));
            p.setTimestamp(5, new Timestamp(clientEntity.getRegistrationDate().getTime()));
            p.setString(6, clientEntity.getEmail());
            p.setString(7, clientEntity.getPhone());
            p.setString(8, clientEntity.getGenderCode());
            p.setString(9, clientEntity.getPhotoPath());
            p.setInt(10, clientEntity.getId());
            System.out.println(p);
            try {
                p.executeUpdate();
            } catch (Exception ex) {
                throw new SQLException("Изменение не удалось");
            }
            return true;
        }
    }

    public static void delete(int id) throws SQLException {
        try (Connection c = Main.getConnection()) {
            String sql = "delete from Client where ID = ? ";

            PreparedStatement p = c.prepareStatement(sql);
            p.setInt(1, id);
            System.out.println(p);
            p.executeUpdate();
        }
    }
}

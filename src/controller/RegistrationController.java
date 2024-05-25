package controller;

import model.User;
import model.User.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationController {

    public static User addUserToDatabase(String name, String email, String phone, String address, String password, Role role) {
        User user = null;
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name,email,phone,address,password,role) " +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, role.toString()); // Menyimpan peran sebagai string

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                user.setPassword(password);
                user.setRole(role); // Set peran user
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }
}

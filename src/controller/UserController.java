package src.controller;

import java.sql.*;
import src.db.DBConnection;
import src.model.User;

public class UserController {

    public static boolean registerUser(User user) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(name, email, username, password, address) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAddress());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Username already exists
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // login success if user found
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

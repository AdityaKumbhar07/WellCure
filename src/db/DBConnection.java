package src.db;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class DBConnection {
    private static Connection conn;

    public static Connection getConnection() {
        if (conn != null) return conn;

        try (InputStream input = new FileInputStream("resources/db.properties")) {
            Properties props = new Properties();
            props.load(input);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");

            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}

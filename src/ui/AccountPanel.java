package src.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import src.db.DBConnection;

public class AccountPanel extends JFrame {
    private String username;

    public AccountPanel(String username) {
        this.username = username;

        setTitle("My Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Account Details", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 18));

        JTextArea userDetails = new JTextArea();
        userDetails.setEditable(false);
        userDetails.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(userDetails);
        JButton backBtn = new JButton("Back to Dashboard");

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        loadUserDetails(userDetails);

        backBtn.addActionListener(e -> {
            dispose();
            new UserDashboardPanel(username);
        });

        setVisible(true);
    }

    private void loadUserDetails(JTextArea userDetails) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "Name     : " + rs.getString("name") + "\n"
                        + "Email    : " + rs.getString("email") + "\n"
                        + "Username : " + rs.getString("username") + "\n"
                        + "Address  : " + rs.getString("address") + "\n";

                userDetails.setText(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            userDetails.setText("Unable to load user details.");
        }
    }
}

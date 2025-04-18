package src.ui;

import src.ui.StartWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginPanel extends JFrame {
    public AdminLoginPanel() {
        setTitle("Admin Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        add(usernameLabel); add(usernameField);
        add(passwordLabel); add(passwordField);
        add(loginBtn); add(backBtn);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword()).trim();

                // Only one hardcoded admin
                if (user.equals("admin") && pass.equals("admin123")) {
                    JOptionPane.showMessageDialog(null, "Welcome Admin!");
                    dispose();
                    new AdminDashboardPanel(); // We'll build this next
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid admin credentials.");
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartWindow();
            }
        });

        setVisible(true);
    }
}

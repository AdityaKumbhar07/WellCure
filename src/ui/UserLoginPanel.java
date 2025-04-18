package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.controller.UserController;

public class UserLoginPanel extends JFrame {
    public UserLoginPanel() {
        setTitle("User Login");
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
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username and password!");
                    return;
                }

                boolean valid = UserController.loginUser(username, password);

                if (valid) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose();
                    // Forward to UploadPrescriptionPanel (next screen)
                    new UserDashboardPanel(username); // We'll create this next
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
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

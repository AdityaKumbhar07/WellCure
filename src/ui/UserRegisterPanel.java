package src.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.model.User;
import src.controller.UserController;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class UserRegisterPanel extends JFrame {
    public UserRegisterPanel() {
        setTitle("User Registration");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

//        JLabel nameLabel = new JLabel("Name:");
//        JTextField nameField = new JTextField();
//
//        JLabel emailLabel = new JLabel("Email:");
//        JTextField emailField = new JTextField();
//
//        JLabel usernameLabel = new JLabel("Username:");
//        JTextField usernameField = new JTextField();
//
//        JLabel passwordLabel = new JLabel("Password:");
//        JPasswordField passwordField = new JPasswordField();
//
//        JLabel addressLabel = new JLabel("Address:");
//        JTextField addressField = new JTextField();
//
//        JButton registerBtn = new JButton("Register");
//        JButton backBtn = new JButton("Back");
//
//        add(nameLabel); add(nameField);
//        add(emailLabel); add(emailField);
//        add(usernameLabel); add(usernameField);
//        add(passwordLabel); add(passwordField);
//        add(addressLabel); add(addressField);
//        add(registerBtn); add(backBtn);

        JLabel headingLabel = new JLabel("Register");
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("NAME");
        JTextField nameField = new JTextField();

        JLabel usernameLabel = new JLabel("USERNAME");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("PASSWORD");
        JPasswordField passwordField = new JPasswordField();

        JLabel emailLabel = new JLabel("EMAIL");
        JTextField emailField = new JTextField();

        JLabel addressLabel = new JLabel("ADDRESS");
        JTextField addressField = new JTextField();

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        Border roundedBorder = BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 2, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backBtn.setBorder(roundedBorder);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(Color.BLACK);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorder(roundedBorder);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Heading
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(headingLabel, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(addressLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(addressField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(backBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(registerBtn, gbc);

        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String address = addressField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                User user = new User(name, email, username, password, address);
                boolean success = UserController.registerUser(user);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Registered Successfully!");
                    dispose();
                    new StartWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "User already exists or error occurred.");
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

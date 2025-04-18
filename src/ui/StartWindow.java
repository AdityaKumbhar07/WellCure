package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartWindow extends JFrame {
    public StartWindow() {
        setTitle("Welcome to WellCure");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

//        JLabel heading = new JLabel("WELL CURE", JLabel.CENTER);
//        heading.setFont(new Font("Arial", Font.BOLD, 35));
//
//        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
//        JButton userLoginBtn = new JButton("User Login");
//        JButton userRegisterBtn = new JButton("Register");
//        JButton adminLoginBtn = new JButton("Admin Login");
//
//        buttonPanel.add(userLoginBtn);
//        buttonPanel.add(userRegisterBtn);
//        buttonPanel.add(adminLoginBtn);
//
//        add(heading, BorderLayout.NORTH);
//        add(buttonPanel, BorderLayout.CENTER);

//new style for buttons

        JLabel heading = new JLabel("WELL CURE", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 40));

// Create buttons
        JButton userLoginBtn = new JButton("User Login");
        JButton userRegisterBtn = new JButton("Register");
        JButton adminLoginBtn = new JButton("Admin Login");

// Style buttons
        JButton[] buttons = { userLoginBtn, userRegisterBtn, adminLoginBtn };
        for (JButton btn : buttons) {
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.PLAIN, 20));
        }

// Create a button panel with GridLayout for vertical stacking
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15));
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

// Center the button panel and add padding
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.white); // Optional: match your window background
        centerPanel.add(buttonPanel);

// Add to main layout
        add(heading, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);



// Actions for buttons

        userLoginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new src.ui.UserLoginPanel();
            }
        });

        userRegisterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserRegisterPanel();
            }
        });

        adminLoginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminLoginPanel();
            }
        });


        setVisible(true);
    }

    private void styleButton(JButton userLoginBtn) {

    }

}

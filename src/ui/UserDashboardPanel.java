package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboardPanel extends JFrame {
    private String username;

    public UserDashboardPanel(String username) {
        this.username = username;

        setTitle("User Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton viewMedicinesBtn = new JButton("View Available Medicines");
        JButton uploadPrescriptionBtn = new JButton("Upload Prescription");
        JButton cartBtn = new JButton("View Cart");
        JButton accountBtn = new JButton("My Account");
        JButton logoutBtn = new JButton("Logout");

        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        centerPanel.add(viewMedicinesBtn);
        centerPanel.add(uploadPrescriptionBtn);
        centerPanel.add(cartBtn);
        centerPanel.add(accountBtn);
        centerPanel.add(logoutBtn);

        add(welcomeLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        viewMedicinesBtn.addActionListener(e -> {
            dispose();
            new ViewMedicinesPanel(username); // We'll create this next
        });

        uploadPrescriptionBtn.addActionListener(e -> {
            dispose();
            new UploadPrescriptionPanel(username); // Modified version to save in cart only
        });

        cartBtn.addActionListener(e -> {
            dispose();
            new CartPanel(username); // We'll create this later
        });

        accountBtn.addActionListener(e -> {
            dispose();
            new AccountPanel(username); // User info panel
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new StartWindow();
        });

        setVisible(true);
    }
}

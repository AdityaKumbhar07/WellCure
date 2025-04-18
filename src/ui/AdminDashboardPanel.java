package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboardPanel extends JFrame {
    public AdminDashboardPanel() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Welcome to Admin Panel", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JButton manageOrdersBtn = new JButton("View Order Requests");
        JButton manageStockBtn = new JButton("Manage Stock");
        JButton generateReportBtn = new JButton("Generate Report");
        JButton logoutBtn = new JButton("Logout");

        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        panel.add(manageOrdersBtn);
        panel.add(manageStockBtn);
        panel.add(generateReportBtn);
        panel.add(logoutBtn);

        add(heading, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        manageOrdersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new OrderRequestPanel(); // Will build next
            }
        });

        manageStockBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StockManagementPanel(); // Will build after that
            }
        });

        generateReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // We'll build actual export logic in util later
                JOptionPane.showMessageDialog(null, "Report generation coming soon.");
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartWindow();
            }
        });

        setVisible(true);
    }
}

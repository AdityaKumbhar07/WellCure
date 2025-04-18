package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;

public class ViewMedicinesPanel extends JFrame {
    private JTable medicineTable;
    private DefaultTableModel model;
    private String username;

    public ViewMedicinesPanel(String username) {
        this.username = username;

        setTitle("Available Medicines");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Available Medicines", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"ID", "Name", "Type", "Price", "Stock"}, 0);
        medicineTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        JButton backBtn = new JButton("Back to Dashboard");

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        loadMedicines();

        backBtn.addActionListener(e -> {
            dispose();
            new UserDashboardPanel(username);
        });

        setVisible(true);
    }

    private void loadMedicines() {
        model.setRowCount(0); // Clear existing rows
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM medicines");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                model.addRow(new Object[]{id, name, type, price, stock});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

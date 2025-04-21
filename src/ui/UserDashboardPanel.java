package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import src.db.DBConnection;

public class UserDashboardPanel extends JFrame {
    private String username;
    private JTable medicineTable;
    private DefaultTableModel model;

    public UserDashboardPanel(String username) {
        this.username = username;

        setTitle("User Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP panel with Back & Account
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("⬅ Back");
        JButton accountBtn = new JButton("Account 👤");

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(accountBtn, BorderLayout.EAST);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search 🔍");

        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        // Medicine table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Type", "Price", "Stock"}, 0);
        medicineTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        // Bottom buttons: Upload + Orders
        JPanel bottomPanel = new JPanel();
        JButton uploadBtn = new JButton("📤 Upload Prescription");
        JButton ordersBtn = new JButton("🛒 My Orders");

        bottomPanel.add(uploadBtn);
        bottomPanel.add(ordersBtn);


        // Adding components to main layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        backBtn.addActionListener(e -> {
            dispose();
            new StartWindow(); // You can change this to a logout confirmation if needed
        });

        accountBtn.addActionListener(e -> {
            dispose();
            new AccountPanel(username);
        });

        uploadBtn.addActionListener(e -> {
            dispose();
            new UploadPrescriptionPanel(username);
        });

        ordersBtn.addActionListener(e -> {
            dispose();
            new CartPanel(username); // ← opens the order history & cart page
        });

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            filterMedicines(query);
        });

        loadAllMedicines();
        setVisible(true);
    }


    private void loadAllMedicines() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM medicines");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterMedicines(String keyword) {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM medicines");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name").toLowerCase();
                String type = rs.getString("type").toLowerCase();

                if (name.contains(keyword) || type.contains(keyword)) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

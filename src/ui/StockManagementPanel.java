package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;

public class StockManagementPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public StockManagementPanel() {
        setTitle("Manage Stock");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Medicine Stock", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"ID", "Name", "Type", "Price", "Stock"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addBtn = new JButton("Add Medicine");
        JButton modifyBtn = new JButton("Modify Stock");
        JButton backBtn = new JButton("Back");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(modifyBtn);
        bottomPanel.add(backBtn);

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMedicines();

        addBtn.addActionListener(e -> addMedicine());
        modifyBtn.addActionListener(e -> modifyStock());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardPanel();
        });

        setVisible(true);
    }

    private void loadMedicines() {
        model.setRowCount(0); // clear table first
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

    private void addMedicine() {
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(new JLabel("Stock:")); panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO medicines(name, type, price, stock) VALUES (?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, type);
                ps.setDouble(3, price);
                ps.setInt(4, stock);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine Added Successfully!");
                loadMedicines();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
            }
        }
    }

    private void modifyStock() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine first.");
            return;
        }

        int medicineId = (int) model.getValueAt(selectedRow, 0);
        String medicineName = model.getValueAt(selectedRow, 1).toString();

        String newStockStr = JOptionPane.showInputDialog(this, "Enter new stock for " + medicineName + ":");
        if (newStockStr != null) {
            try {
                int newStock = Integer.parseInt(newStockStr);

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE medicines SET stock = ? WHERE id = ?");
                ps.setInt(1, newStock);
                ps.setInt(2, medicineId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                loadMedicines();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid stock value.");
            }
        }
    }
}

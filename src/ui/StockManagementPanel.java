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
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Medicine Inventory", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"ID", "Name", "Type", "Price", "Stock"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addBtn = new JButton("Add Medicine");
        JButton modifyBtn = new JButton("Modify Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton backBtn = new JButton("Back");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(modifyBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMedicines();

        addBtn.addActionListener(e -> addMedicine());
        modifyBtn.addActionListener(e -> modifySelectedMedicine());
        deleteBtn.addActionListener(e -> deleteSelectedMedicine());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardPanel();
        });

        setVisible(true);
    }

    private void loadMedicines() {
        model.setRowCount(0); // Clear table
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

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO medicines(name, type, price, stock) VALUES (?, ?, ?, ?)");
                ps.setString(1, nameField.getText().trim());
                ps.setString(2, typeField.getText().trim());
                ps.setDouble(3, Double.parseDouble(priceField.getText().trim()));
                ps.setInt(4, Integer.parseInt(stockField.getText().trim()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine Added!");
                loadMedicines();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }

    private void modifySelectedMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine first!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String name = model.getValueAt(row, 1).toString();
        String type = model.getValueAt(row, 2).toString();
        String price = model.getValueAt(row, 3).toString();
        String stock = model.getValueAt(row, 4).toString();

        JTextField nameField = new JTextField(name);
        JTextField typeField = new JTextField(type);
        JTextField priceField = new JTextField(price);
        JTextField stockField = new JTextField(stock);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(new JLabel("Stock:")); panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE medicines SET name = ?, type = ?, price = ?, stock = ? WHERE id = ?");
                ps.setString(1, nameField.getText().trim());
                ps.setString(2, typeField.getText().trim());
                ps.setDouble(3, Double.parseDouble(priceField.getText().trim()));
                ps.setInt(4, Integer.parseInt(stockField.getText().trim()));
                ps.setInt(5, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine Updated!");
                loadMedicines();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }

    private void deleteSelectedMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine to delete!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this medicine?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM medicines WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine Deleted!");
                loadMedicines();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete.");
            }
        }
    }
}

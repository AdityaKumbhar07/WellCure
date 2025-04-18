package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;

public class AddMedicineToOrderPanel extends JFrame {
    private JTable orderTable, medicineTable;
    private DefaultTableModel orderModel, medicineModel;
    private int selectedOrderId = -1;
    private int selectedMedicineId = -1;

    public AddMedicineToOrderPanel() {
        setTitle("Add Medicines to Order");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel buttonPanel = new JPanel();

        orderModel = new DefaultTableModel(new String[]{"Order ID", "User ID", "Total", "Status"}, 0);
        orderTable = new JTable(orderModel);
        loadOrders();

        medicineModel = new DefaultTableModel(new String[]{"Medicine ID", "Name", "Type", "Price", "Stock"}, 0);
        medicineTable = new JTable(medicineModel);
        loadMedicines();

        topPanel.add(new JScrollPane(orderTable));
        topPanel.add(new JScrollPane(medicineTable));

        JButton addBtn = new JButton("Add Selected Medicine to Selected Order");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(addBtn);
        buttonPanel.add(backBtn);

        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row >= 0) selectedOrderId = Integer.parseInt(orderModel.getValueAt(row, 0).toString());
        });

        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            int row = medicineTable.getSelectedRow();
            if (row >= 0) selectedMedicineId = Integer.parseInt(medicineModel.getValueAt(row, 0).toString());
        });

        addBtn.addActionListener(e -> addMedicineToOrder());

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardPanel();
        });

        setVisible(true);
    }

    private void loadOrders() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE status = 'Confirmed'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orderModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMedicines() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM medicines");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                medicineModel.addRow(new Object[]{
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

    private void addMedicineToOrder() {
        if (selectedOrderId == -1 || selectedMedicineId == -1) {
            JOptionPane.showMessageDialog(this, "Please select both order and medicine.");
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        try {
            int qty = Integer.parseInt(qtyStr);

            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement("SELECT price, stock FROM medicines WHERE id = ?");
            ps1.setInt(1, selectedMedicineId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                JOptionPane.showMessageDialog(this, "Medicine not found.");
                return;
            }

            double price = rs1.getDouble("price");
            int stock = rs1.getInt("stock");

            if (qty > stock) {
                JOptionPane.showMessageDialog(this, "Not enough stock available.");
                return;
            }

            PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO order_items(order_id, medicine_id, quantity) VALUES (?, ?, ?)");
            ps2.setInt(1, selectedOrderId);
            ps2.setInt(2, selectedMedicineId);
            ps2.setInt(3, qty);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE medicines SET stock = stock - ? WHERE id = ?");
            ps3.setInt(1, qty);
            ps3.setInt(2, selectedMedicineId);
            ps3.executeUpdate();

            PreparedStatement ps4 = con.prepareStatement(
                    "UPDATE orders SET total_price = total_price + ? WHERE id = ?");
            ps4.setDouble(1, qty * price);
            ps4.setInt(2, selectedOrderId);
            ps4.executeUpdate();

            JOptionPane.showMessageDialog(this, "Medicine added to order!");
            dispose();
            new AddMedicineToOrderPanel();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding medicine.");
        }
    }
}

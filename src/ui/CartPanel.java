package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;

public class CartPanel extends JFrame {
    private JTable cartTable;
    private DefaultTableModel model;
    private String username;
    private int selectedOrderId = -1;

    public CartPanel(String username) {
        this.username = username;

        setTitle("Your Cart (Prescriptions)");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Your Pending Orders", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"Order ID", "Prescription ID", "Status"}, 0);
        cartTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        JButton sendBtn = new JButton("Send Order Request");
        JButton backBtn = new JButton("Back to Dashboard");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendBtn);
        bottomPanel.add(backBtn);

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadCartItems();

        cartTable.getSelectionModel().addListSelectionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row >= 0) {
                selectedOrderId = Integer.parseInt(model.getValueAt(row, 0).toString());
            }
        });

        sendBtn.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first!");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
                ps.setString(1, "Requested");
                ps.setInt(2, selectedOrderId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Order Request Sent!");
                dispose();
                new CartPanel(username); // Refresh cart
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update order status.");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new UserDashboardPanel(username);
        });

        setVisible(true);
    }

    private void loadCartItems() {
        model.setRowCount(0); // Clear table
        try {
            Connection con = DBConnection.getConnection();

            // Get user_id using username
            PreparedStatement ps1 = con.prepareStatement("SELECT id FROM users WHERE username = ?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();

            int userId = -1;
            if (rs1.next()) {
                userId = rs1.getInt("id");
            }

            if (userId != -1) {
                PreparedStatement ps2 = con.prepareStatement(
                        "SELECT id, prescription_id, status FROM orders WHERE user_id = ? AND status = 'Pending in Cart'");
                ps2.setInt(1, userId);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    int id = rs2.getInt("id");
                    int presId = rs2.getInt("prescription_id");
                    String status = rs2.getString("status");

                    model.addRow(new Object[]{id, presId, status});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;

public class OrderHistoryPanel extends JFrame {
    private String username;
    private JTable table;
    private DefaultTableModel model;
    private int selectedOrderId = -1;

    public OrderHistoryPanel(String username) {
        this.username = username;

        setTitle("Your Orders");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Your Orders", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"Order ID", "Total Price", "Status", "Payment Mode"}, 0);
        table = new JTable(model);
        loadOrders();

        JScrollPane scrollPane = new JScrollPane(table);

        JButton checkoutBtn = new JButton("Checkout Selected");
        JButton backBtn = new JButton("Back");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(checkoutBtn);
        bottomPanel.add(backBtn);

        add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedOrderId = Integer.parseInt(model.getValueAt(row, 0).toString());
            }
        });

        checkoutBtn.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Confirm checkout for Order No: " + selectedOrderId + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                checkoutOrder();
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new StartWindow();
        });

        setVisible(true);
    }

    private void loadOrders() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("id");
            }

            if (userId != -1) {
                PreparedStatement ps2 = con.prepareStatement(
                        "SELECT id, total_price, status, payment_mode FROM orders WHERE user_id = ?");
                ps2.setInt(1, userId);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    int id = rs2.getInt("id");
                    double price = rs2.getDouble("total_price");
                    String status = rs2.getString("status");
                    String mode = rs2.getString("payment_mode");

                    model.addRow(new Object[]{id, price, status, mode});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkoutOrder() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE orders SET payment_mode = ?, status = ? WHERE id = ?");
            ps.setString(1, "Cash on Delivery");
            ps.setString(2, "Confirmed");
            ps.setInt(3, selectedOrderId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order No: " + selectedOrderId + " has been checked out with Cash on Delivery.");
            dispose();
            new OrderHistoryPanel(username); // Refresh screen
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Checkout failed. Please try again.");
        }
    }
}

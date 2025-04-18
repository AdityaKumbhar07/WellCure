package src.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import src.db.DBConnection;
import java.io.File;

public class OrderRequestPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private int selectedOrderId = -1;
    private String selectedPrescriptionPath = null;

    public OrderRequestPanel() {
        setTitle("Order Requests");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Prescription Uploads", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 18));

        model = new DefaultTableModel(new String[]{"Order ID", "User ID", "Prescription Path", "Status"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton viewPrescriptionBtn = new JButton("View Prescription");
        JButton confirmBtn = new JButton("Confirm Order");
        JButton rejectBtn = new JButton("Reject Order");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewPrescriptionBtn);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(backBtn);

        add(heading, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadOrderRequests();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedOrderId = Integer.parseInt(model.getValueAt(row, 0).toString());
                selectedPrescriptionPath = model.getValueAt(row, 2).toString();
            }
        });

        viewPrescriptionBtn.addActionListener(e -> {
            if (selectedPrescriptionPath == null) {
                JOptionPane.showMessageDialog(this, "Please select an order first.");
                return;
            }

            try {
                Desktop.getDesktop().open(new File(selectedPrescriptionPath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not open image.");
                ex.printStackTrace();
            }
        });

        confirmBtn.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order.");
                return;
            }
            updateOrderStatus("Confirmed");
        });

        rejectBtn.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order.");
                return;
            }
            updateOrderStatus("Rejected");
        });

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardPanel();
        });

        setVisible(true);
    }

    private void loadOrderRequests() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT o.id, o.user_id, p.image_path, o.status " +
                    "FROM orders o JOIN prescriptions p ON o.prescription_id = p.id " +
                    "WHERE o.status = 'Requested'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String imagePath = rs.getString("image_path");
                String status = rs.getString("status");

                model.addRow(new Object[]{id, userId, imagePath, status});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatus(String status) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE orders SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, selectedOrderId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order " + status + "!");
            dispose();
            new OrderRequestPanel(); // Refresh
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update order status.");
        }
    }
}

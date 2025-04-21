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
    private String selectedStatus = "";

    public CartPanel(String username) {
        this.username = username;

        setTitle("Your Orders (Cart & History)");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Your Orders", JLabel.CENTER);
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

        loadOrders();

        cartTable.getSelectionModel().addListSelectionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row >= 0) {
                selectedOrderId = Integer.parseInt(model.getValueAt(row, 0).toString());
                selectedStatus = model.getValueAt(row, 2).toString();
            }
        });

        sendBtn.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first!");
                return;
            }

            if (!selectedStatus.equals("Pending in Cart")) {
                JOptionPane.showMessageDialog(this, "Only 'Pending in Cart' orders can be requested.");
                return;
            }

            showPaymentMethodDialog();
        });

        backBtn.addActionListener(e -> {
            dispose();
            new UserDashboardPanel(username);
        });

        setVisible(true);
    }

    private void loadOrders() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement("SELECT id FROM users WHERE username = ?");
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();

            int userId = -1;
            if (rs1.next()) {
                userId = rs1.getInt("id");
            }

            if (userId != -1) {
                PreparedStatement ps2 = con.prepareStatement(
                        "SELECT id, prescription_id, status FROM orders WHERE user_id = ?");
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

    private void showPaymentMethodDialog() {
        JRadioButton codBtn = new JRadioButton("Cash on Delivery (COD)");
        codBtn.setSelected(true);
        JRadioButton upiBtn = new JRadioButton("UPI (Coming Soon)");
        upiBtn.setEnabled(false);
        JRadioButton netBankBtn = new JRadioButton("Net Banking (Coming Soon)");
        netBankBtn.setEnabled(false);

        ButtonGroup group = new ButtonGroup();
        group.add(codBtn);
        group.add(upiBtn);
        group.add(netBankBtn);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(new JLabel("Choose Payment Method:"));
        panel.add(codBtn);
        panel.add(upiBtn);
        panel.add(netBankBtn);

        int result = JOptionPane.showConfirmDialog(this, panel, "Payment Method", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE orders SET status = ?, payment_method = ? WHERE id = ?");
                ps.setString(1, "Requested");
                ps.setString(2, "Cash on Delivery");
                ps.setInt(3, selectedOrderId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Order Request Sent Successfully!");
                dispose();
                new CartPanel(username); // refresh
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to send order request.");
            }
        }
    }
}

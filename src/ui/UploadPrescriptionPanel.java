package src.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.sql.*;
import src.db.DBConnection;

public class UploadPrescriptionPanel extends JFrame {
    private String username;

    public UploadPrescriptionPanel(String username) {
        this.username = username;

        setTitle("Upload Prescription");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel("Upload Your Prescription Image", JLabel.CENTER);
        JButton uploadBtn = new JButton("Upload");
        JButton backBtn = new JButton("Back");

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(uploadBtn);
        panel.add(backBtn);

        add(label, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        uploadBtn.addActionListener(e -> uploadPrescription());

        backBtn.addActionListener(e -> {
            dispose();
            new UserDashboardPanel(username);
        });


        setVisible(true);
    }

    private void uploadPrescription() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();

            if (!selected.getName().endsWith(".jpg") && !selected.getName().endsWith(".png")) {
                JOptionPane.showMessageDialog(this, "Only JPG or PNG files allowed.");
                return;
            }

            try {
                // Save image to "uploads" folder
                String uploadDir = "uploads";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdir();

                String fileName = System.currentTimeMillis() + "_" + selected.getName();
                Path target = Paths.get(uploadDir, fileName);
                Files.copy(selected.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

                // Get user_id from username
                Connection con = DBConnection.getConnection();
                PreparedStatement ps1 = con.prepareStatement("SELECT id FROM users WHERE username = ?");
                ps1.setString(1, username);
                ResultSet rs = ps1.executeQuery();

                int userId = -1;
                if (rs.next()) {
                    userId = rs.getInt("id");
                }

                if (userId == -1) {
                    JOptionPane.showMessageDialog(this, "User not found!");
                    return;
                }

                // Insert into prescriptions table
                PreparedStatement ps2 = con.prepareStatement(
                        "INSERT INTO prescriptions(user_id, image_path, status) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps2.setInt(1, userId);
                ps2.setString(2, target.toString());
                ps2.setString(3, "Pending");
                ps2.executeUpdate();

                ResultSet generatedKeys = ps2.getGeneratedKeys();
                int prescriptionId = -1;
                if (generatedKeys.next()) {
                    prescriptionId = generatedKeys.getInt(1);
                }

                // Create empty order linked to this prescription
                if (prescriptionId != -1) {
                    PreparedStatement ps3 = con.prepareStatement(
                            "INSERT INTO orders(user_id, prescription_id, total_price, status, payment_mode) VALUES (?, ?, 0, ?, ?)"
                    );
                    ps3.setInt(1, userId);
                    ps3.setInt(2, prescriptionId);
                    ps3.setString(3, "Requested");
                    ps3.setString(4, "Pending");
                    ps3.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Prescription Uploaded for Order No: " + prescriptionId);
                    dispose();
                    new OrderHistoryPanel(username); // We'll create this next
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Upload failed. Try again.");
            }
        }
    }
}

package view;

import controller.DatabaseConnection;
import controller.CustomerController;
import model.Player;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerForm extends JFrame {
    private JPanel panel1;
    private JTable tablePlayer;
    private JButton logoutButton;
    private JButton exitButton;
    private JTextArea TransferHub;
    private JTextField bidTextField;
    private JButton bidAndAddToButton;
    private JButton checkoutButton;
    private JButton deleteButton;
    private JFrame parentFrame;
    private User currentUser;

    public CustomerForm(JFrame parent, User currentUser) {
        this.currentUser = currentUser;
        setTitle("FC Barcelona Store");
        setContentPane(panel1);
        setSize(1150, 474);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(parent);
        displayPlayerData();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(parentFrame);
                loginForm.showForm();
            }
        });
        bidAndAddToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Periksa apakah JTextArea TransferHub sudah terisi
                if (!TransferHub.getText().isEmpty()) {
                    // Jika JTextArea sudah terisi, tampilkan pesan notifikasi
                    JOptionPane.showMessageDialog(CustomerForm.this, "Please clear TransferHub before placing another bid or checkout your cart first!", "Try Again!", JOptionPane.ERROR_MESSAGE);
                    return; // Hentikan eksekusi lebih lanjut
                }

                // Lanjutkan eksekusi normal jika JTextArea kosong
                int selectedRow = tablePlayer.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(CustomerForm.this, "Please select a player from the table.", "Try Again!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Dapatkan nilai sell_price dari pemain yang dipilih
                BigDecimal sellPrice = (BigDecimal) tablePlayer.getValueAt(selectedRow, 7);

                // Periksa apakah sell_price pemain sudah 0
                if (sellPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    // Jika sell_price sudah 0 atau negatif, tampilkan pesan kesalahan
                    JOptionPane.showMessageDialog(CustomerForm.this, "Player has been sold", "Sorry", JOptionPane.ERROR_MESSAGE);
                    return; // Hentikan eksekusi lebih lanjut
                }

                // Lanjutkan eksekusi normal jika sell_price tidak 0
                int playerId = (int) tablePlayer.getValueAt(selectedRow, 0);
                String playerName = (String) tablePlayer.getValueAt(selectedRow, 1);
                BigDecimal bidPrice = new BigDecimal(bidTextField.getText().trim());

                // Periksa apakah bid_price lebih besar dari atau sama dengan sell_price
                if (bidPrice.compareTo(sellPrice) >= 0) {
                    // Jika bid_price valid, tambahkan informasi penawaran ke TransferHub
                    String transferInfo = "Player ID: " + playerId + ", Name: " + playerName + ", Bid Amount: " + bidPrice + ", User ID: " + currentUser.getUserId() + "\n";
                    TransferHub.append(transferInfo);
                } else {
                    // Jika bid_price tidak valid, tampilkan pesan kesalahan
                    JOptionPane.showMessageDialog(CustomerForm.this, "Bid rejected. Please bid more!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransferHub.setText("");
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date sellingDate = new Date();
                String[] transferInfos = TransferHub.getText().split("\n");

                try (Connection conn = DatabaseConnection.getConnection()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(CustomerForm.this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int id_selling = -1;
                    String sql = "INSERT INTO selling (id, id_player, selling_date, total_payment) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        preparedStatement.setInt(1, currentUser.getUserId());

                        int selectedRow = tablePlayer.getSelectedRow();
                        String playerName = "";
                        BigDecimal bidPrice = BigDecimal.ZERO;
                        if (selectedRow != -1) {
                            playerName = (String) tablePlayer.getValueAt(selectedRow, 1);
                            String bidText = bidTextField.getText().trim();
                            if (bidText.isEmpty()) {
                                JOptionPane.showMessageDialog(CustomerForm.this, "Please enter a bid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            bidPrice = new BigDecimal(bidText);
                        } else {
                            JOptionPane.showMessageDialog(CustomerForm.this, "Please select a player to checkout.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        preparedStatement.setInt(2, (int) tablePlayer.getValueAt(selectedRow, 0));

                        // Set selling price to 0 for the selected player
                        setPlayerSellingPriceToZero((int) tablePlayer.getValueAt(selectedRow, 0), conn);

                        preparedStatement.setTimestamp(3, new Timestamp(sellingDate.getTime()));

                        BigDecimal totalPayment = BigDecimal.ZERO;
                        for (String transferInfo : transferInfos) {
                            if (!transferInfo.isEmpty()) {
                                String bidAmountString = transferInfo.substring(transferInfo.indexOf("Bid Amount: ") + "Bid Amount: ".length(), transferInfo.indexOf(", User ID"));
                                BigDecimal bidAmount = new BigDecimal(bidAmountString.trim());
                                totalPayment = totalPayment.add(bidAmount);
                            }
                        }
                        preparedStatement.setBigDecimal(4, totalPayment);

                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    id_selling = generatedKeys.getInt(1); // Get the generated ID selling
                                } else {
                                    throw new SQLException("Failed to get ID selling, no rows affected.");
                                }
                            }
                            // Get additional user information
                            String userPhone = currentUser.getPhone();
                            String userAddress = currentUser.getAddress();
                            // Format the selling date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            JOptionPane.showMessageDialog(CustomerForm.this, "Checkout successful!\nID Selling: " + id_selling + "\nPlayer Name: " + playerName + "\nBid Price: " + bidPrice + "\nChecked out by: " + currentUser.getName() + " | " + currentUser.getEmail() + "\nUser Phone: " + userPhone + "\nUser Address: " + userAddress + "\nSelling Date: " + dateFormat.format(sellingDate) + "\n" + "\nPLEASE SCREENSHOT THIS NOTIFICATION AS A RECEIPT" + "\nSend the screenshot by email to laporta@barca.es to continue the payment" , "Thank You", JOptionPane.INFORMATION_MESSAGE);
                            TransferHub.setText("");
                            displayPlayerData();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CustomerForm.this, "Failed to perform checkout", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }

    private void displayPlayerData() {
        CustomerController.displayPlayerData(tablePlayer);
    }

    private void setPlayerSellingPriceToZero(int playerId, Connection conn) throws SQLException {
        CustomerController.setPlayerSellingPriceToZero(playerId, conn);
    }
}
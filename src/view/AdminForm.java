package view;

import controller.DatabaseConnection;
import controller.PlayerController;
import model.Player;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminForm extends JFrame {
    private JPanel panel1;
    private JButton Exit;
    private JButton logoutButton;
    private JTable tableplayer;
    private JTextField tfPlayer;
    private JTextField tfCitizenship;
    private JTextField tfAge;
    private JTextField tfMarketvalue;
    private JTextField tfSellprice;
    private JComboBox<String> tfPosition;
    private JComboBox tfFoot;
    private JButton createButton;
    private JButton updateButton;
    private JButton clearButton;
    private JButton findIDButton;
    private JButton deleteButton;
    private JTextField tfSearch;
    private JTable tableselling;
    private JFrame parentFrame;

    public AdminForm(JFrame parent) {
        setTitle("FC Barcelona's Office");
        setContentPane(panel1);
        setSize(1350, 474);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(parent);
        List<String> positions = PlayerController.getAllPositions();
        DefaultComboBoxModel<String> positionComboBoxModel = new DefaultComboBoxModel<>(positions.toArray(new String[0]));
        tfPosition.setModel(positionComboBoxModel);
        List<String> foots = PlayerController.getAllFoot();
        DefaultComboBoxModel<String> footComboBoxModel = new DefaultComboBoxModel<>(foots.toArray(new String[0]));
        tfFoot.setModel(footComboBoxModel);
        displayPlayerData();
        displaySellingData();
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menutup AdminForm
                dispose();

                // Membuat instance dari LoginForm dan menampilkannya
                LoginForm loginForm = new LoginForm(parentFrame);
                loginForm.showForm();
            }
        });

        // Menambahkan ActionListener untuk tabel pemain
        tableplayer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Memeriksa apakah seleksi di tabel berubah
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableplayer.getSelectedRow();
                    if (selectedRow != -1) { // Memastikan ada baris yang dipilih
                        // Mendapatkan nilai dari baris yang dipilih
                        String playerName = tableplayer.getValueAt(selectedRow, 1).toString();
                        String position = tableplayer.getValueAt(selectedRow, 2).toString();
                        String citizenship = tableplayer.getValueAt(selectedRow, 3).toString();
                        String age = tableplayer.getValueAt(selectedRow, 4).toString();
                        String foot = tableplayer.getValueAt(selectedRow, 5).toString();
                        String marketValue = tableplayer.getValueAt(selectedRow, 6).toString();
                        String sellPrice = tableplayer.getValueAt(selectedRow, 7).toString();

                        // Mengatur nilai JTextField dan JComboBox sesuai dengan nilai yang dipilih
                        tfPlayer.setText(playerName);
                        tfPosition.setSelectedItem(position);
                        tfCitizenship.setText(citizenship);
                        tfAge.setText(age);
                        tfFoot.setSelectedItem(foot);
                        tfMarketvalue.setText(marketValue);
                        tfSellprice.setText(sellPrice);
                    }
                }
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ambil nilai dari field dan combo box
                String playerName = tfPlayer.getText();
                String citizenship = tfCitizenship.getText();
                int age = Integer.parseInt(tfAge.getText());
                BigDecimal marketValue = new BigDecimal(tfMarketvalue.getText());
                BigDecimal sellPrice = new BigDecimal(tfSellprice.getText());
                Player.Position position = Player.Position.valueOf((String) tfPosition.getSelectedItem());
                Player.Foot foot = Player.Foot.valueOf((String) tfFoot.getSelectedItem());

                // Buat objek Player baru
                Player newPlayer = new Player();
                newPlayer.setName(playerName);
                newPlayer.setPosition(position);
                newPlayer.setCitizenship(citizenship);
                newPlayer.setAge(age);
                newPlayer.setFoot(foot);
                newPlayer.setMarketValue(marketValue);
                newPlayer.setSellPrice(sellPrice);

                // Simpan data ke database
                PlayerController.savePlayerToDatabase(newPlayer);

                // Setelah menyimpan data, Anda dapat memperbarui tampilan tabel jika diperlukan
                displayPlayerData();
            }
        });
        findIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int searchId = Integer.parseInt(tfSearch.getText());
                searchById(searchId);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlayerData();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Membersihkan nilai pada JTextField
                tfPlayer.setText("");
                tfCitizenship.setText("");
                tfAge.setText("");
                tfMarketvalue.setText("");
                tfSellprice.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();
            }
        });

    }

    private void displayPlayerData() {
        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mendapatkan data dari database
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Position");
        model.addColumn("Citizenship");
        model.addColumn("Age");
        model.addColumn("Foot");
        model.addColumn("Market Value");
        model.addColumn("Sell Price");

        String sql = "SELECT * FROM players";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Player player = new Player();
                player.setId(resultSet.getInt("id_player")); // Mengambil id_player
                player.setName(resultSet.getString("name_player"));
                player.setPosition(Player.Position.valueOf(resultSet.getString("position")));
                player.setCitizenship(resultSet.getString("citizenship"));
                player.setAge(resultSet.getInt("age"));
                player.setFoot(Player.Foot.valueOf(resultSet.getString("foot")));
                player.setMarketValue(resultSet.getBigDecimal("market_value"));
                player.setSellPrice(resultSet.getBigDecimal("sell_price"));

                model.addRow(new Object[]{
                        player.getId(), // Menambah id_player ke dalam tabel
                        player.getName(),
                        player.getPosition(),
                        player.getCitizenship(),
                        player.getAge(),
                        player.getFoot(),
                        player.getMarketValue(),
                        player.getSellPrice()
                });
            }

            // Set model tabel
            tableplayer.setModel(model);

            // Tutup koneksi
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchById(int id) {
        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mendapatkan data dari database berdasarkan ID
        String sql = "SELECT * FROM players WHERE id_player = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Player player = new Player();
                player.setId(resultSet.getInt("id_player"));
                player.setName(resultSet.getString("name_player"));
                player.setPosition(Player.Position.valueOf(resultSet.getString("position")));
                player.setCitizenship(resultSet.getString("citizenship"));
                player.setAge(resultSet.getInt("age"));
                player.setFoot(Player.Foot.valueOf(resultSet.getString("foot")));
                player.setMarketValue(resultSet.getBigDecimal("market_value"));
                player.setSellPrice(resultSet.getBigDecimal("sell_price"));

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Name");
                model.addColumn("Position");
                model.addColumn("Citizenship");
                model.addColumn("Age");
                model.addColumn("Foot");
                model.addColumn("Market Value");
                model.addColumn("Sell Price");

                model.addRow(new Object[]{
                        player.getId(),
                        player.getName(),
                        player.getPosition(),
                        player.getCitizenship(),
                        player.getAge(),
                        player.getFoot(),
                        player.getMarketValue(),
                        player.getSellPrice()
                });

                // Set model tabel
                tableplayer.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "Player with ID " + id + " not found", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }

            // Tutup koneksi
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlayerData() {
        int row = tableplayer.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a player to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int playerId = (int) tableplayer.getValueAt(row, 0);
        String playerName = tfPlayer.getText();
        String citizenship = tfCitizenship.getText();
        int age = Integer.parseInt(tfAge.getText());
        BigDecimal marketValue = new BigDecimal(tfMarketvalue.getText());
        BigDecimal sellPrice = new BigDecimal(tfSellprice.getText());
        Player.Position position = Player.Position.valueOf((String) tfPosition.getSelectedItem());
        Player.Foot foot = Player.Foot.valueOf((String) tfFoot.getSelectedItem());

        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);
        updatedPlayer.setName(playerName);
        updatedPlayer.setPosition(position);
        updatedPlayer.setCitizenship(citizenship);
        updatedPlayer.setAge(age);
        updatedPlayer.setFoot(foot);
        updatedPlayer.setMarketValue(marketValue);
        updatedPlayer.setSellPrice(sellPrice);

        PlayerController.updatePlayerInDatabase(updatedPlayer);

        // Setelah menyimpan data, Anda dapat memperbarui tampilan tabel jika diperlukan
        displayPlayerData();
    }

    private void deletePlayer() {
        int row = tableplayer.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a player to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int playerId = (int) tableplayer.getValueAt(row, 0);

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this player?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            PlayerController.deletePlayerFromDatabase(playerId);
            displayPlayerData(); // Refresh table after deletion
        }
    }

    private void displaySellingData() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Selling");
        model.addColumn("User ID");
        model.addColumn("Player ID");
        model.addColumn("Player Name"); // Tambah kolom untuk nama player
        model.addColumn("Selling Date");
        model.addColumn("Total Payment");
        model.addColumn("User Email"); // Tambah kolom untuk email user

        String sql = "SELECT selling.id_selling, selling.id, selling.id_player, players.name_player, selling.selling_date, selling.total_payment, users.email " +
                "FROM selling " +
                "INNER JOIN players ON selling.id_player = players.id_player " +
                "INNER JOIN users ON selling.id = users.id";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("id_selling"),
                        resultSet.getInt("id"),
                        resultSet.getInt("id_player"),
                        resultSet.getString("name_player"),
                        resultSet.getDate("selling_date"),
                        resultSet.getBigDecimal("total_payment"),
                        resultSet.getString("email")
                });
            }

            tableselling.setModel(model);

            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

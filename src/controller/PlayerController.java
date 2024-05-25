//PlayerController.java
package controller;

import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerController {

    public static void savePlayerToDatabase(Player player) {
        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Menyimpan data player ke database
        String sql = "INSERT INTO players (name_player, position, citizenship, age, foot, market_value, sell_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getPosition().toString());
            preparedStatement.setString(3, player.getCitizenship());
            preparedStatement.setInt(4, player.getAge());
            preparedStatement.setString(5, player.getFoot().toString());
            preparedStatement.setBigDecimal(6, player.getMarketValue());
            preparedStatement.setBigDecimal(7, player.getSellPrice());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "New player added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add new player", "Error", JOptionPane.ERROR_MESSAGE);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add new player", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();

        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return playerList;
        }

        // Mendapatkan data dari database
        String sql = "SELECT * FROM players";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Player player = new Player();
                player.setName(resultSet.getString("name_player"));
                player.setPosition(Player.Position.valueOf(resultSet.getString("position")));
                player.setCitizenship(resultSet.getString("citizenship"));
                player.setAge(resultSet.getInt("age"));
                player.setFoot(Player.Foot.valueOf(resultSet.getString("foot")));
                player.setMarketValue(resultSet.getBigDecimal("market_value"));
                player.setSellPrice(resultSet.getBigDecimal("sell_price"));

                playerList.add(player);
            }

            // Tutup koneksi
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return playerList;
    }

    public static void displayPlayerData(DefaultTableModel model) {
        List<Player> playerList = getAllPlayers();

        // Menampilkan data ke dalam tabel
        for (Player player : playerList) {
            model.addRow(new Object[]{
                    player.getName(),
                    player.getPosition(),
                    player.getCitizenship(),
                    player.getAge(),
                    player.getFoot(),
                    player.getMarketValue(),
                    player.getSellPrice()
            });
        }
    }

    public static List<String> getAllPositions() {
        List<String> positions = new ArrayList<>();
        for (Player.Position position : Player.Position.values()) {
            positions.add(position.toString());
        }
        return positions;
    }

    public static List<String> getAllFoot() {
        List<String> foots = new ArrayList<>();
        for (Player.Foot foot : Player.Foot.values()) {
            foots.add(foot.toString());
        }
        return foots;
    }

    public static void updatePlayerInDatabase(Player player) {
        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Memperbarui data player di database
        String sql = "UPDATE players SET name_player = ?, position = ?, citizenship = ?, age = ?, foot = ?, market_value = ?, sell_price = ? WHERE id_player = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getPosition().toString());
            preparedStatement.setString(3, player.getCitizenship());
            preparedStatement.setInt(4, player.getAge());
            preparedStatement.setString(5, player.getFoot().toString());
            preparedStatement.setBigDecimal(6, player.getMarketValue());
            preparedStatement.setBigDecimal(7, player.getSellPrice());
            preparedStatement.setInt(8, player.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Player data updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update player data", "Error", JOptionPane.ERROR_MESSAGE);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update player data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void deletePlayerFromDatabase(int playerId) {
        // Mendapatkan koneksi database
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Menghapus data player dari database
        String sql = "DELETE FROM players WHERE id_player = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, playerId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Player data deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete player data", "Error", JOptionPane.ERROR_MESSAGE);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete player data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

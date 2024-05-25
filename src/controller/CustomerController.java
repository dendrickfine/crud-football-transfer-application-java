package controller;

import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.*;

public class CustomerController {
    public static void displayPlayerData(JTable tablePlayer) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                player.setId(resultSet.getInt("id_player"));
                player.setName(resultSet.getString("name_player"));
                player.setPosition(Player.Position.valueOf(resultSet.getString("position")));
                player.setCitizenship(resultSet.getString("citizenship"));
                player.setAge(resultSet.getInt("age"));
                player.setFoot(Player.Foot.valueOf(resultSet.getString("foot")));
                player.setMarketValue(resultSet.getBigDecimal("market_value"));
                player.setSellPrice(resultSet.getBigDecimal("sell_price"));

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
            }

            tablePlayer.setModel(model);

            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setPlayerSellingPriceToZero(int playerId, Connection conn) throws SQLException {
        String updateSql = "UPDATE players SET sell_price = ? WHERE id_player = ?";
        PreparedStatement updateStatement = conn.prepareStatement(updateSql);
        updateStatement.setBigDecimal(1, BigDecimal.ZERO);
        updateStatement.setInt(2, playerId);
        updateStatement.executeUpdate();
        updateStatement.close();
    }
}

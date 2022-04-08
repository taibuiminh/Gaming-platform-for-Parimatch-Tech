package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPlayer implements DAO<Player, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(DAOPlayer.class);

  @Override
  public Player get(Integer id) throws SQLException {
    String getPlayer = "SELECT * FROM player WHERE player_id = ?";
    Player player = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getPlayer)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int playerId = resultSet.getInt("player_id");
        Timestamp date = resultSet.getTimestamp("registration_date");
        int status = resultSet.getInt("status");
        int country = resultSet.getInt("country");
        double balance = resultSet.getDouble("balance");
        String currency = resultSet.getString("currency");
        player = new Player(playerId, date, status, country, balance, currency);
      }
    }
    return player;
  }

  @Override
  public List getAll() throws SQLException {
    String getPlayer = "SELECT * FROM player ";
    List<Player> listOfPlayers = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getPlayer)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int playerId = resultSet.getInt("player_id");
        Timestamp date = resultSet.getTimestamp("registration_date");
        int status = resultSet.getInt("status");
        int country = resultSet.getInt("country");
        double balance = resultSet.getDouble("balance");
        String currency = resultSet.getString("currency");
        Player player = new Player(playerId, date, status, country, balance, currency);
        listOfPlayers.add(player);
      }
    }
    return listOfPlayers;
  }

  @Override
  public void save(Player player) throws SQLException {
    String insertAccountScrtipt = "INSERT INTO player (\"player_id\", \"registration_date\", \"status\", \"country\", \"balance\", \"currency\") VALUES (?, ?, ?, ?, ?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScrtipt)) {
      preparedStatement.setInt(1, player.getPlayerId());
      preparedStatement.setTimestamp(2, player.getRegistrationDate());
      preparedStatement.setInt(3, player.getStatus());
      preparedStatement.setInt(4, player.getCountry());
      preparedStatement.setDouble(5, player.getBalance());
      preparedStatement.setString(6, player.getCurrency());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public Player update(Player player) throws SQLException {
    String updatePlayerScript = "UPDATE player SET \"registration_date\" = ?, \"status\" = ?, \"country\" = ?, \"balance\" = ?, \"currency\" = ? WHERE \"player_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updatePlayerScript)) {
      preparedStatement.setTimestamp(1, player.getRegistrationDate());
      preparedStatement.setInt(2, player.getStatus());
      preparedStatement.setInt(3, player.getCountry());
      preparedStatement.setDouble(4, player.getBalance());
      preparedStatement.setString(5, player.getCurrency());
      preparedStatement.setInt(6, player.getPlayerId());
      preparedStatement.executeUpdate();
    }
    return player;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deletePlayer = "DELETE FROM player WHERE player_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deletePlayer)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}

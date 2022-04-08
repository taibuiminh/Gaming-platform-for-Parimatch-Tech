package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOGame implements DAO<Game, String> {
  private static final Logger logger = LoggerFactory.getLogger(DAOGame.class);

  @Override
  public Game get(String id) throws SQLException {
    String getGame = "SELECT * FROM game WHERE game_id = ?";
    Game game = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getGame)) {
      preparedStatement.setString(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String gameId = resultSet.getString("game_id");
        int statusId = resultSet.getInt("status_id");
        game = new Game(gameId, statusId);
      }
    }
    return game;
  }

  @Override
  public List getAll() throws SQLException {
    String getGame = "SELECT * FROM game ";
    List<Game> listOfGames = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getGame)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String gameId = resultSet.getString("game_id");
        int statusId = resultSet.getInt("status_id");
        Game game = new Game(gameId, statusId);
        listOfGames.add(game);
      }
    }
    return listOfGames;
  }

  @Override
  public void save(Game game) throws SQLException {
    String insertAccountScript = "INSERT INTO game (\"game_id\", \"status_id\") VALUES (?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScript)) {
      preparedStatement.setString(1, game.getGameId());
      preparedStatement.setInt(2, game.getStatusId());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public Game update(Game game) throws SQLException {
    String updateGameScript = "UPDATE game SET  \"status_id\" = ? WHERE \"game_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateGameScript)) {
      preparedStatement.setInt(1, game.getStatusId());
      preparedStatement.setString(2, game.getGameId());
      preparedStatement.executeUpdate();
    }
    return game;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deleteGame = "DELETE FROM game WHERE game_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteGame)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}

package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.GameRound;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOGameRound implements DAO<GameRound, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(DAOGameRound.class);

  @Override
  public GameRound get(Integer id) throws SQLException {
    String getGameRound = "SELECT * FROM game_round WHERE round_id = ?";
    GameRound gameRound = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getGameRound)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int roundId = resultSet.getInt("round_id");
        int playerId = resultSet.getInt("player_id");
        String gameId = resultSet.getString("game_id");
        Timestamp startTime = resultSet.getTimestamp("start_time");
        Timestamp endTime = resultSet.getTimestamp("end_time");
        boolean isFinished = resultSet.getBoolean("is_finished");
        gameRound = new GameRound(roundId, playerId, gameId, startTime, endTime, isFinished);
      }
    }
    return gameRound;
  }

  @Override
  public List getAll() throws SQLException {
    String getGameRound = "SELECT * FROM game_round ";
    List<GameRound> listOfGameRounds = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getGameRound)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int roundId = resultSet.getInt("round_id");
        int playerId = resultSet.getInt("player_id");
        String gameId = resultSet.getString("game_id");
        Timestamp startTime = resultSet.getTimestamp("start_time");
        Timestamp endTime = resultSet.getTimestamp("end_time");
        boolean isFinished = resultSet.getBoolean("is_finished");
        GameRound gameRound = new GameRound(roundId, playerId, gameId, startTime, endTime, isFinished);
        listOfGameRounds.add(gameRound);
      }
    }
    return listOfGameRounds;
  }

  @Override
  public void save(GameRound gameRound) throws SQLException {
    String insertAccountScrtipt = "INSERT INTO game_round (\"round_id\", \"player_id\", \"game_id\", \"start_time\", \"end_time\", \"is_finished\") VALUES (?, ?, ?, ?, ?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScrtipt)) {
      preparedStatement.setInt(1, gameRound.getRoundId());
      preparedStatement.setInt(2, gameRound.getPlayerId());
      preparedStatement.setString(3, gameRound.getGameId());
      preparedStatement.setTimestamp(4, gameRound.getStartTime());
      preparedStatement.setTimestamp(5, gameRound.getEndTime());
      preparedStatement.setBoolean(6, gameRound.getFinished());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public GameRound update(GameRound gameRound) throws SQLException {
    String updateGameRoundScript = "UPDATE game_round SET \"player_id\" = ?, \"game_id\" = ?, \"start_time\" = ?, \"end_time\" = ?, \"is_finished\" = ? WHERE \"round_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateGameRoundScript)) {
      preparedStatement.setInt(1, gameRound.getPlayerId());
      preparedStatement.setString(2, gameRound.getGameId());
      preparedStatement.setTimestamp(3, gameRound.getStartTime());
      preparedStatement.setTimestamp(4, gameRound.getEndTime());
      preparedStatement.setBoolean(5, gameRound.getFinished());
      preparedStatement.setInt(6, gameRound.getRoundId());
      preparedStatement.executeUpdate();
    }
    return gameRound;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deleteRound = "DELETE FROM game_round WHERE round_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteRound)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}

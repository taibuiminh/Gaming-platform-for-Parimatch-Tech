package parimatch.project.startapp.utilStart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;
import parimatch.project.simplewallet.DB.DBConnection;

import java.sql.*;

public class StartHelperDB {
  private static final String DATA_BASE_NAME = Config.getSetting("DATA_BASE_NAME");
  private static final Logger logger = LoggerFactory.getLogger(StartHelperDB.class);

  public static boolean createDataBase() {
    try (Connection connection = DBConnection.getFirstConnection();
         Statement statement = connection.createStatement()) {
      if (!checkDBExists(DATA_BASE_NAME)) {
        statement.executeUpdate("CREATE DATABASE " + DATA_BASE_NAME);
        logger.info("Successfully created new the data base");
        statement.close();
        return true;
      } else {
        logger.info("Data Base {} already exist", DATA_BASE_NAME);
      }
    } catch (SQLException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public static boolean dropDataBase() {
    try (Connection connection = DBConnection.getFirstConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate("DROP DATABASE " + DATA_BASE_NAME + " WITH (FORCE)");
      logger.info("Successfully dropped the data base");
      return true;
    } catch (SQLException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public static boolean checkDBExists(String dataBase) {
    String query = "SELECT datname FROM pg_database;";
    try (Connection connection = DBConnection.getFirstConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String databaseName = resultSet.getString(1);
        if (dataBase.equals(databaseName)) {
          logger.info("Data Base exists");
          return true;
        }
      }
      resultSet.close();
    } catch (SQLException e) {
      logger.error("ERROR was occurred: {}", e.getMessage());
      e.printStackTrace();
    }
    logger.info("DataBase does not exist");
    return false;
  }
}

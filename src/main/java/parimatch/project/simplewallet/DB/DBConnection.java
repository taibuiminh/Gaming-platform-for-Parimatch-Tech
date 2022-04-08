package parimatch.project.simplewallet.DB;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
  private static final String DB_URL = Config.getSetting("DB_URL");
  private static final String USERNAME = Config.getSetting("USERNAME");
  private static final String PASSWORD = Config.getSetting("PASSWORD");
  private static final String DATA_BASE_NAME = Config.getSetting("DATA_BASE_NAME");
  private static final BasicDataSource ds = new BasicDataSource();

  static {
    try {
      Class.forName("org.postgresql.Driver");
      ds.setUrl(DB_URL + DATA_BASE_NAME);
      ds.setUsername(USERNAME);
      ds.setPassword(PASSWORD);
      ds.setMinIdle(10);
      ds.setMaxIdle(100);
      ds.setMaxOpenPreparedStatements(100);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  public static Connection getConnection() throws SQLException {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return ds.getConnection();
  }

  public static Connection getFirstConnection() {
    Connection connection = null;
    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    } catch (ClassNotFoundException | SQLException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
}

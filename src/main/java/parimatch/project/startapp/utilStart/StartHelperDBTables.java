package parimatch.project.startapp.utilStart;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StartHelperDBTables {
  private static final Logger logger = LoggerFactory.getLogger(StartHelperDBTables.class);

  public static boolean createTables(String filename) throws IOException {
    try (Connection connection = DBConnection.getConnection()) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      logger.info("Getting sql file from resources...");


      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      try (InputStream resourceStream = loader.getResourceAsStream(filename)) {
        Reader reader = new BufferedReader(new InputStreamReader(resourceStream));
        logger.info("Running the script...");
        scriptRunner.runScript(reader);
        System.out.println("Created tables");
        return true;
      }
    } catch (SQLException | FileNotFoundException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public static boolean dropAllTables() {
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("DROP SCHEMA public CASCADE;\n" +
                 "CREATE SCHEMA public;")) {
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public static boolean addRoundTrigger() {
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "CREATE OR REPLACE FUNCTION update_end_time_of_round()\n" +
                         "RETURNS TRIGGER AS $$\n" +
                         "BEGIN\n" +
                         "   IF now() >= OLD.start_time + interval '10 second'  THEN\n" +
                         "   NEW.end_time = now() &&\n" +
                         "     NEW.is_finished = true;\n" +
                         "RETURN NEW;\n" +
                         "ELSE\n" +
                         "      RETURN OLD;\n" +
                         "END IF;\n" +
                         "END;\n" +
                         "$$ language 'plpgsql';\n" +
                         "\n" +
                         "\n" +
                         "\n" +
                         "\n" +
                         "\n" +
                         "CREATE TRIGGER update_end_time_of_round BEFORE UPDATE ON game_round FOR EACH ROW EXECUTE PROCEDURE  update_end_time_of_round();")) {
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      logger.error("Error was occurred: {}", e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
}

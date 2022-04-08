package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOStatus implements DAO<Status, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(DAOStatus.class);

  @Override
  public Status get(Integer id) throws SQLException {
    String getStatus = "SELECT * FROM status WHERE status_id = ?";
    Status status = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getStatus)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int statusId = resultSet.getInt("status_id");
        String stateName = resultSet.getString("state_name");
        String description = resultSet.getString("description");
        status = new Status(statusId, stateName, description);
      }
    }
    return status;
  }

  @Override
  public List getAll() throws SQLException {
    String getStatus = "SELECT * FROM status ";
    List<Status> listOfStatuses = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getStatus)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int statusId = resultSet.getInt("status_id");
        String stateName = resultSet.getString("state_name");
        String description = resultSet.getString("description");
        Status status = new Status(statusId, stateName, description);
        listOfStatuses.add(status);
      }
    }
    return listOfStatuses;
  }

  @Override
  public void save(Status status) throws SQLException {
    String insertAccountScrtipt = "INSERT INTO status (\"status_id\", \"state_name\", \"description\") VALUES (?, ?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScrtipt)) {
      preparedStatement.setInt(1, status.getStatusId());
      preparedStatement.setString(2, status.getStateName());
      preparedStatement.setString(3, status.getDescription());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public Status update(Status status) throws SQLException {
    String updateStatusScript = "UPDATE status SET \"state_name\" = ?, \"description\" = ? WHERE \"status_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateStatusScript)) {
      preparedStatement.setString(1, status.getStateName());
      preparedStatement.setString(2, status.getDescription());
      preparedStatement.setInt(3, status.getStatusId());
      preparedStatement.executeUpdate();
    }
    return status;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deleteStatus = "DELETE FROM status WHERE status_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteStatus)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}

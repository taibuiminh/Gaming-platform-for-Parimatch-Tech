package parimatch.project.simplewallet.DB.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dto.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOTransaction implements DAO<Transaction, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(DAOTransaction.class);

  @Override
  public Transaction get(Integer id) throws SQLException {
    String getTransaction = "SELECT * FROM transaction WHERE tx_id = ?";
    Transaction transaction = null;
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getTransaction)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int txId = resultSet.getInt("tx_id");
        int roundId = resultSet.getInt("round_id");
        int amount = resultSet.getInt("amount");
        String type = resultSet.getString("type");
        Timestamp timeStamp = resultSet.getTimestamp("time_stamp");
        String walletTxId = resultSet.getString("wallet_tx_id");
        String currency = resultSet.getString("currency");
        transaction = new Transaction(txId, roundId, amount, type, timeStamp, walletTxId, currency);
      }
    }
    return transaction;
  }

  @Override
  public List getAll() throws SQLException {
    String getCountry = "SELECT * FROM transaction ";
    List<Transaction> listOfTransactions = new ArrayList<>();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(getCountry)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int txId = resultSet.getInt("tx_id");
        int roundId = resultSet.getInt("round_id");
        int amount = resultSet.getInt("amount");
        String type = resultSet.getString("type");
        Timestamp timeStamp = resultSet.getTimestamp("time_stamp");
        String walletTxId = resultSet.getString("wallet_tx_id");
        String currency = resultSet.getString("currency");
        Transaction transaction = new Transaction(txId, roundId, amount, type, timeStamp, walletTxId, currency);
        listOfTransactions.add(transaction);
      }
    }
    return listOfTransactions;
  }

  @Override
  public void save(Transaction transaction) throws SQLException {
    String insertAccountScrtipt = "INSERT INTO transaction (\"tx_id\", \"round_id\", \"amount\", \"type\", \"time_stamp\", \"wallet_tx_id\", \"currency\") VALUES (?, ?, ?, ?, ?, ?, ?);";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(insertAccountScrtipt)) {
      preparedStatement.setInt(1, transaction.getTxId());
      preparedStatement.setInt(2, transaction.getRoundId());
      preparedStatement.setInt(3, transaction.getAmount());
      preparedStatement.setString(4, transaction.getType());
      preparedStatement.setTimestamp(5, transaction.getTimeStamp());
      preparedStatement.setString(6, transaction.getWalletTxId());
      preparedStatement.setString(7, transaction.getCurrency());
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public Transaction update(Transaction transaction) throws SQLException {
    String updateTransactionScript = "UPDATE transaction SET \"round_id\" = ?, \"amount\" = ?, \"type\" = ?, \"time_stamp\" = ?, \"wallet_tx_id\" = ?, \"currency\" = ? WHERE \"tx_id\" = ?;";
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateTransactionScript)) {
      preparedStatement.setInt(1, transaction.getRoundId());
      preparedStatement.setInt(2, transaction.getAmount());
      preparedStatement.setString(3, transaction.getType());
      preparedStatement.setTimestamp(4, transaction.getTimeStamp());
      preparedStatement.setString(5, transaction.getWalletTxId());
      preparedStatement.setString(6, transaction.getCurrency());
      preparedStatement.setInt(7, transaction.getTxId());
      preparedStatement.executeUpdate();
    }
    return transaction;
  }

  @Override
  public void delete(int id) throws SQLException {
    String deleteTransaction = "DELETE FROM transaction WHERE tx_id = ?";
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteTransaction)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }
  }
}

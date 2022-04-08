package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;
import parimatch.project.simplewallet.DB.DBConnection;
import parimatch.project.simplewallet.DB.dao.DAOTransaction;
import parimatch.project.simplewallet.DB.dto.Transaction;
import parimatch.project.simplewallet.communicationmodels.TransactionResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
  private static final String DATA_BASE_NAME = Config.getSetting("DATA_BASE_NAME");
  DAOTransaction daoTransaction = new DAOTransaction();

  public Transaction getTransactionById(int id) throws SQLException {
    logger.info("Get transaction with id {}", id);
    return daoTransaction.get(id);
  }

  public Transaction updateTransaction(Transaction transaction) throws SQLException {
    logger.info("Update transaction with id {}", transaction.getTxId());
    daoTransaction.update(transaction);
    return transaction;
  }

  public void deleteTransactionById(int id) throws SQLException {
    logger.info("Delete transaction with id {}", id);
    daoTransaction.delete(id);
  }

  public Transaction insertTransaction(Transaction transaction) throws SQLException {
    logger.info("Insert transaction with id {}", transaction.getTxId());
    daoTransaction.save(transaction);
    return transaction;
  }

  public List<Transaction> getAllTransactions() throws SQLException {
    logger.info("Get all transactions");
    return daoTransaction.getAll();
  }

  public Boolean existsTransaction(Transaction transaction) throws SQLException {
    logger.info("Exists transaction with id {}", transaction.getTxId());
    return getTransactionById(transaction.getTxId()) != null;
  }

  public boolean transactionWalletTxIdExistsInDatabase(TransactionResponse walletTxId) throws SQLException {
    logger.info("Exists transactionWallet with id {}", walletTxId.getWalletTxId());
    String getTransaction = "SELECT * FROM transaction WHERE wallet_tx_id = ?";
    String wallet_tx_id = walletTxId.getWalletTxId();
    logger.info("Trying to connect to DB...");
    try (Connection connection = DBConnection.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(getTransaction);
      preparedStatement.setString(1, wallet_tx_id);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    }
  }
}



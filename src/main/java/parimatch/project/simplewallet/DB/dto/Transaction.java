package parimatch.project.simplewallet.DB.dto;

import java.sql.Timestamp;

public class Transaction {
  Integer txId;
  Integer roundId;
  Integer amount;
  String type;
  Timestamp timeStamp;
  String walletTxId;
  String currency;

  public Transaction(Integer txId, Integer roundId, Integer amount, String type, long timeStamp, String walletTxId,
                     String currency) {
    this.txId = txId;
    this.roundId = roundId;
    this.amount = amount;
    this.type = type;
    this.timeStamp = new Timestamp(timeStamp);
    this.walletTxId = walletTxId;
    this.currency = currency;
  }

  public Transaction(Integer txId, Integer roundId, Integer amount, String type, Timestamp timeStamp, String walletTxId,
                     String currency) {
    this.txId = txId;
    this.roundId = roundId;
    this.amount = amount;
    this.type = type;
    this.timeStamp = timeStamp;
    this.walletTxId = walletTxId;
    this.currency = currency;
  }

  public Integer getTxId() {
    return txId;
  }

  public Integer getRoundId() {
    return roundId;
  }

  public Integer getAmount() {
    return amount;
  }

  public String getType() {
    return type;
  }

  public Timestamp getTimeStamp() {
    return timeStamp;
  }

  public String getWalletTxId() {
    return walletTxId;
  }

  public String getCurrency() {
    return currency;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "txId=" + txId +
            ", roundId=" + roundId +
            ", amount=" + amount +
            ", type='" + type + '\'' +
            ", timeStamp='" + timeStamp + '\'' +
            ", walletTxId='" + walletTxId + '\'' +
            ", currency='" + currency + '\'' +
            '}';
  }
}

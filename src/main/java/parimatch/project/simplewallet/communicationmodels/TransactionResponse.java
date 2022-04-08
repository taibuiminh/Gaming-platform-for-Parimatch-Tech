package parimatch.project.simplewallet.communicationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResponse {

  private final String playerId;
  private final String originalTxId;
  private final String walletTxId;
  private final String currency;
  private Double balance;

  @JsonCreator
  public TransactionResponse(
          @JsonProperty("playerId") String playerId,
          @JsonProperty("originalTxId") String originalTxId,
          @JsonProperty("walletTxId") String walletTxId,
          @JsonProperty("balance") Double balance,
          @JsonProperty("currency") String currency) {
    this.playerId = playerId;
    this.originalTxId = String.valueOf(originalTxId);
    this.walletTxId = walletTxId;
    this.balance = balance;
    this.currency = currency;
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getOriginalTxId() {
    return originalTxId;
  }

  public Integer retrieveOriginalTxIdAsInteger() {
    return Integer.parseInt(getOriginalTxId());
  }

  public String getWalletTxId() {
    return walletTxId;
  }

  public Double getBalance() {
    return balance;
  }

  public String getCurrency() {
    return currency;
  }

  public void withdrawFromBalance(double amount) {
    this.balance -= amount;
  }

  public void depositIntoBalance(double amount) {
    this.balance += amount;
  }

  @Override
  public String toString() {
    return "TransactionResponse{" +
            "playerId=" + playerId +
            ", originalTxId=" + originalTxId +
            ", walletTxId=" + walletTxId +
            ", balance=" + balance +
            ", currency='" + currency + '\'' +
            '}';
  }
}

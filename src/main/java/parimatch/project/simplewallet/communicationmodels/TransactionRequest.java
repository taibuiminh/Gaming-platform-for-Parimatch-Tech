package parimatch.project.simplewallet.communicationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TransactionRequest {

  private final String playerId;
  private final Double amount;
  private final String currency;
  private final TransactionInformation transactionInformation;

  @JsonCreator
  public TransactionRequest(
          @JsonProperty("playerId") String playerId,
          @JsonProperty("amount") double amount,
          @JsonProperty("currency") String currency,
          @JsonProperty("transaction") TransactionInformation transactionInformation) {
    this.playerId = playerId;
    this.amount = amount;
    this.currency = currency;
    this.transactionInformation = transactionInformation;
  }

  public String getPlayerId() {
    return playerId;
  }

  public Integer retrievePlayerIdAsInteger() {
    return Integer.parseInt(getPlayerId());
  }

  public Double getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public TransactionInformation getTransaction() {
    return transactionInformation;
  }

  public String getGameId() {
    return transactionInformation.getGameId();
  }

  public String getRoundId() {
    return transactionInformation.getRoundId();
  }

  public Integer retrieveRoundIdAsInteger() {
    return Integer.parseInt(getRoundId());
  }

  public String getTxId() {
    return transactionInformation.getTxId();
  }

  @Override
  public String toString() {
    return "IncomingRequest{" +
            "playerId=" + playerId +
            ", amount=" + amount +
            ", currency='" + currency + '\'' +
            ", transaction=" + transactionInformation +
            '}';
  }

  public List<String> getAllValuesAsStrings() {

    return new ArrayList<>(List.of(
            playerId, amount.toString(), currency,
            getGameId(), getRoundId(), getTxId()
    ));
  }
}

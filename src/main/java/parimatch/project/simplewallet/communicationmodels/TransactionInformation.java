package parimatch.project.simplewallet.communicationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionInformation {

  private final String gameId;
  private final String roundId;
  private final String txId;

  @JsonCreator
  public TransactionInformation(
          @JsonProperty("gameId") String gameId,
          @JsonProperty("roundId") String roundId,
          @JsonProperty("txId") String txId) {
    this.gameId = gameId;
    this.roundId = roundId;
    this.txId = txId;
  }

  public String getGameId() {
    return gameId;
  }

  public String getRoundId() {
    return roundId;
  }

  public String getTxId() {
    return txId;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "gameId='" + gameId + '\'' +
            ", roundId=" + roundId +
            ", txId=" + txId +
            '}';
  }
}

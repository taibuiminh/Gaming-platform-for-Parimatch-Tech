package parimatch.project.simplewallet.communicationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerAccountInformationResponse {

  private final String playerId;
  private final Double balance;
  private final String currency;

  @JsonCreator
  public PlayerAccountInformationResponse(
          @JsonProperty("playerId") String playerId,
          @JsonProperty("balance") Double balance,
          @JsonProperty("currency") String currency) {
    this.playerId = playerId;
    this.balance = balance;
    this.currency = currency;
  }

  public String getPlayerId() {
    return playerId;
  }

  public Double getBalance() {
    return balance;
  }

  public String getCurrency() {
    return currency;
  }

  @Override
  public String toString() {
    return "PlayerAccountInformation{" +
            "playerId='" + playerId + '\'' +
            ", balance=" + balance +
            ", currency=" + currency +
            '}';
  }
}

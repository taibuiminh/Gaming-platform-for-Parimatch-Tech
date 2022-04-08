package parimatch.project.gameintegrationservice.utilprocessors.gameflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Round {

  @JsonProperty("playerId")
  Integer playerId;
  Integer roundId;
  String gameId;

  public Round() {
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public Integer getRoundId() {
    return roundId;
  }

  public String getGameId() {
    return gameId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Round round = (Round) o;
    return playerId.equals(round.playerId) && roundId.equals(round.roundId) && gameId.equals(round.gameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, roundId, gameId);
  }

  @JsonProperty("transaction")
  private void unpackNestedJson(Map<String, String> transaction) {
    this.roundId = Integer.parseInt(transaction.get("roundId"));
    this.gameId = transaction.get("gameId");
  }

  @Override
  public String toString() {
    return "Round{" +
            "playerId=" + playerId +
            ", roundId=" + roundId +
            ", gameId='" + gameId + '\'' +
            '}';
  }
}

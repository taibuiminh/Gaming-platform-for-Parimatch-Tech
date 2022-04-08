package parimatch.project.simplewallet.DB.dto;

import java.sql.Timestamp;

public class GameRound {
  Integer roundId;
  Integer playerId;
  String gameId;
  Timestamp startTime;
  Timestamp endTime;
  Boolean isFinished;

  public GameRound(Integer roundId, Integer playerId, String gameId, Timestamp startTime, Timestamp endTime,
                   Boolean isFinished) {
    this.roundId = roundId;
    this.playerId = playerId;
    this.gameId = gameId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isFinished = isFinished;
  }

  public Integer getRoundId() {
    return roundId;
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public String getGameId() {
    return gameId;
  }

  public Timestamp getStartTime() {
    return startTime;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }

  public Boolean getFinished() {
    return isFinished;
  }

  public void setFinished(Boolean finished) {
    isFinished = finished;
  }

  @Override
  public String toString() {
    return "GameRound{" +
            "roundId=" + roundId +
            ", playerId=" + playerId +
            ", gameId=" + gameId +
            ", startTime='" + startTime + '\'' +
            ", endTime='" + endTime + '\'' +
            ", isFinished=" + isFinished +
            '}';
  }
}

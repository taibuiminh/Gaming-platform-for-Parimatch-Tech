package parimatch.project.simplewallet.DB.dto;

public class Game {
  String gameId;
  Integer statusId;

  public Game(String gameId, Integer statusId) {
    this.gameId = gameId;
    this.statusId = statusId;

  }

  public String getGameId() {
    return gameId;
  }

  public Integer getStatusId() {
    return statusId;
  }

  @Override
  public String toString() {
    return "Game{" +
            "gameId=" + gameId +
            ", statusId=" + statusId + '\'' +
            '}';
  }
}

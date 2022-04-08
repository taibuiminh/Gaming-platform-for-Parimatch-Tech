package parimatch.project.gameintegrationservice.exceptions;

public class BetNotFoundException extends Exception {
  public BetNotFoundException() {
    super("Bet with specified ID not found!");
  }
}

package parimatch.project.gameintegrationservice.exceptions;

public class BetAlreadySettledException extends Exception {
  public BetAlreadySettledException() {
    super("There is already a bet for this round!");
  }
}

package parimatch.project.gameintegrationservice.exceptions;

public class SameTxIdException extends Exception {
  public SameTxIdException() {
    super("Win and bet have the same txIds!");
  }
}

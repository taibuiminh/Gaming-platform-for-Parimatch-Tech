package parimatch.project.simplewallet.exceptions;

public class RoundIsOverException extends Exception {
  public RoundIsOverException() {
    super("Round for this win request is over!");
  }
}

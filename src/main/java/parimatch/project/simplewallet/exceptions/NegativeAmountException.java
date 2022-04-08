package parimatch.project.simplewallet.exceptions;

public class NegativeAmountException extends Exception {
  public NegativeAmountException() {
    super("The amount of a transaction cannot be request negative");
  }
}

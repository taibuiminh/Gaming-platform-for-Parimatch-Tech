package parimatch.project.simplewallet.exceptions;

public class InsufficientFundsException extends Exception {
  public InsufficientFundsException() {
    super("Player doesn't have enough money for the bet!");
  }
}

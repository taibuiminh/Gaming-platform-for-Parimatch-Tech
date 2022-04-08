package parimatch.project.simplewallet.exceptions;

public class IncorrectCurrenciesException extends Exception {
  public IncorrectCurrenciesException() {
    super("The currencies of a transaction request and the related player balance should match");
  }
}

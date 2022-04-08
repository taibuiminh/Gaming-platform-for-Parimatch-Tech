package parimatch.project.gameintegrationservice.exceptions;

public class SecurityKeyMismatchException extends Exception {
  public SecurityKeyMismatchException() {
    super("Wrong security key!");
  }
}

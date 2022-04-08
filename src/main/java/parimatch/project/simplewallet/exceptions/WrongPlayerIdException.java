package parimatch.project.simplewallet.exceptions;

import java.sql.SQLException;

public class WrongPlayerIdException extends SQLException {
  public WrongPlayerIdException() {
    super("PlayerId was NOT found in the database!");
  }
}

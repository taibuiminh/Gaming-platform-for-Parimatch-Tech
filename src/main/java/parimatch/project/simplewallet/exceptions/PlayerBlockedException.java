package parimatch.project.simplewallet.exceptions;

import java.sql.SQLException;

public class PlayerBlockedException extends SQLException {
  public PlayerBlockedException() {
    super("Player is inactive!");
  }
}

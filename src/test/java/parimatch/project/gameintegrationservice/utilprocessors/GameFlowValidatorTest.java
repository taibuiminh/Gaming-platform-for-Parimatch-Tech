package parimatch.project.gameintegrationservice.utilprocessors;

import org.junit.Test;
import parimatch.project.gameintegrationservice.exceptions.BetAlreadySettledException;
import parimatch.project.gameintegrationservice.exceptions.BetNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameFlowValidatorTest {

  String jsonBodyForTestBet1 = "{\"playerId\": \"13\", \"amount\": 10.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"4321\"} }";
  String jsonBodyForTestBet2 = "{\"playerId\": \"33\", \"amount\": 50.00, \"currency\": \"USD\", \"transaction\": {\"gameId\": \"doots\", \"roundId\": \"2\", \"txId\": \"4322\"} }";
  String jsonBodyForTestWin3 = "{\"playerId\": \"13\", \"amount\": 20.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"4323\"} }";
  String jsonBodyForTestWin4 = "{\"playerId\": \"33\", \"amount\": 100.00, \"currency\": \"USD\", \"transaction\": {\"gameId\": \"doots\", \"roundId\": \"3\", \"txId\": \"4324\"} }";

  @Test
  public void addBetSuccess() throws Exception {
    assertTrue(GameFlowValidator.addBetToHistory(jsonBodyForTestBet2));
  }

  @Test(expected = BetAlreadySettledException.class)
  public void addBetFail() throws Exception {
    assertFalse(GameFlowValidator.addBetToHistory(jsonBodyForTestBet1));
  }

  @Test
  public void checkWinSuccess() throws Exception {
    GameFlowValidator.addBetToHistory(jsonBodyForTestBet1);
    assertTrue(GameFlowValidator.checkWin(jsonBodyForTestWin3));
  }

  @Test(expected = BetNotFoundException.class)
  public void checkWinFail() throws Exception {
    assertFalse(GameFlowValidator.checkWin(jsonBodyForTestWin4));
  }
}
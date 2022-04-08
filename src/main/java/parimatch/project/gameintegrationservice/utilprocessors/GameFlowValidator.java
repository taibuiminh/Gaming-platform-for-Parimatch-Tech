package parimatch.project.gameintegrationservice.utilprocessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.gameintegrationservice.exceptions.BetAlreadySettledException;
import parimatch.project.gameintegrationservice.exceptions.BetNotFoundException;
import parimatch.project.gameintegrationservice.exceptions.SameTxIdException;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.GameActivity;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.Round;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameFlowValidator {

  private static final Map<Round, GameActivity> history = Collections.synchronizedMap(
          new HashMap<>());

  private static final Logger logger = LoggerFactory.getLogger(GameFlowValidator.class);

  public static boolean addBetToHistory(String jsonBodyStr) throws BetAlreadySettledException {
    logger.info("Adding bet");
    Round round = DataParser.generateRound(jsonBodyStr);
    GameActivity bet = DataParser.generateGameActivity(jsonBodyStr, "bet");

    if (roundIsInHistory(round) && roundHasBet(round)) {
      logger.error("{} is already in history and has a related bet", round);
      throw new BetAlreadySettledException();
    } else {
      logger.info("Inserted {} and {} into history", round, bet);
      history.put(round, bet);
      return true;
    }
  }

  private static boolean roundIsInHistory(Round round) {
    logger.info("Checking whether {} is in the history of rounds...", round);
    return history.containsKey(round);
  }

  public static boolean checkWin(String jsonBodyStr) throws BetNotFoundException, SameTxIdException {
    logger.info("Checking win");
    Round round = DataParser.generateRound(jsonBodyStr);
    GameActivity win = DataParser.generateGameActivity(jsonBodyStr, "win");

    if (roundIsInHistory(round) && roundHasBet(round)) {
      logger.info("If round is in the history and it has a bet...");
      if (!winAndBetIdsDiffer(round, win)) {
        throw new SameTxIdException();
      } else {
        return true;
      }
    } else {
      throw new BetNotFoundException();
    }
  }

  private static boolean roundHasBet(Round round) {
    logger.info("Checking whether {} has a related bet", round);
    GameActivity gameActivity = extractRelatedBetTypeFromHistory(round);
    return Objects.equals(gameActivity.getTypeTransaction(), "bet");
  }

  private static boolean winAndBetIdsDiffer(Round round, GameActivity win) {
    logger.info("Checking whether the related bet of a {} matches the given {}", round, win);
    GameActivity gameActivity = extractRelatedBetTypeFromHistory(round);
    return !Objects.equals(gameActivity.getTxId(), win.getTxId());
  }

  private static GameActivity extractRelatedBetTypeFromHistory(Round round) {
    GameActivity gameActivity = history.get(round);
    logger.info("Received {} as a first GameActivity from the list for {}", gameActivity, round);
    return gameActivity;
  }
}

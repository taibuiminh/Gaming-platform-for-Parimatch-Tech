package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;
import parimatch.project.simplewallet.DB.dao.DAOGameRound;
import parimatch.project.simplewallet.DB.dto.GameRound;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class GameRoundService {
  private static final Logger logger = LoggerFactory.getLogger(GameRoundService.class);
  DAOGameRound daoGameRound = new DAOGameRound();

  public GameRound getGameRoundById(int id) throws SQLException {
    logger.info("Get gameRound with id {}", id);
    return daoGameRound.get(id);
  }

  public GameRound updateGameRound(GameRound gameRound) throws SQLException {
    logger.info("Update gameRound with id {}", gameRound.getRoundId());
    daoGameRound.update(gameRound);
    return gameRound;
  }

  public void deleteGameRoundById(int id) throws SQLException {
    logger.info("Delete gameRound with id {}", id);
    daoGameRound.delete(id);
  }

  public GameRound insertGameRound(GameRound gameRound) throws SQLException {
    logger.info("Insert gameRound with id {}", gameRound.getRoundId());
    daoGameRound.save(gameRound);
    return gameRound;
  }

  public List<GameRound> getAllGameRounds() throws SQLException {
    logger.info("Get all gameRounds");
    return daoGameRound.getAll();
  }

  public GameRound updateIfRoundShouldBeEnded(GameRound gameRound) throws SQLException {
    long startTime = gameRound.getStartTime().getTime();
    long currentTime = System.currentTimeMillis();
    long finishTIme = Integer.parseInt(Config.getSetting("SECONDS_FOR_ROUND_FINISH")) * 1000L;
    if ((currentTime - startTime) > finishTIme) {
      gameRound.setFinished(true);
      gameRound.setEndTime(new Timestamp(finishTIme));
      updateGameRound(gameRound);
    }
    return gameRound;
  }
}

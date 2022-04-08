package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dao.DAOGame;
import parimatch.project.simplewallet.DB.dto.Game;

import java.sql.SQLException;
import java.util.List;

public class GameService {
  private static final Logger logger = LoggerFactory.getLogger(GameService.class);
  DAOGame daoGame = new DAOGame();

  public Game getGameById(String id) throws SQLException {
    logger.info("Get game with id {}", id);
    return daoGame.get(id);
  }

  public Game updateGame(Game newGame) throws SQLException {
    logger.info("Update game with id {}", newGame.getGameId());
    daoGame.update(newGame);
    return newGame;
  }

  public void deleteGameById(int id) throws SQLException {
    logger.info("Delete game with id {}", id);
    daoGame.delete(id);
  }

  public Game insertGame(Game game) throws SQLException {
    logger.info("Insert game with id {}", game.getGameId());
    daoGame.save(game);
    return game;
  }

  public List<Game> getAllGames() throws SQLException {
    logger.info("Get all games");
    return daoGame.getAll();
  }


}

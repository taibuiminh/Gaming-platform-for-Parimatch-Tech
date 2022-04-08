package parimatch.project.simplewallet.DB.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dao.DAOPlayer;
import parimatch.project.simplewallet.DB.dto.Player;

import java.sql.SQLException;
import java.util.List;

public class PlayerService {
  private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
  DAOPlayer daoPlayer = new DAOPlayer();

  public Player getPlayerById(int id) throws SQLException {
    logger.info("Get player with id {}", id);
    System.out.println(daoPlayer.get(id));
    return daoPlayer.get(id);
  }

  public Player updatePlayer(Player newPlayer) throws SQLException {
    logger.info("Update player with id {}", newPlayer.getPlayerId());
    daoPlayer.update(newPlayer);
    return daoPlayer.update(newPlayer);
  }

  public void deletePlayerById(int id) throws SQLException {
    logger.info("Delete player with id {}", id);
    daoPlayer.delete(id);
  }

  public Player insertPlayer(Player player) throws SQLException {
    logger.info("Insert player with id {}", player.getPlayerId());
    daoPlayer.save(player);
    return player;
  }

  public List<Player> getAllPlayers() throws SQLException {
    logger.info("Get all players");
    return daoPlayer.getAll();
  }

  public Double getPlayersBalance(int id) throws SQLException {
    logger.info("Get balance player with id {}", id);
    return getPlayerById(id).getBalance();
  }

  public Player updatePlayerBalance(int id, Double balance) throws SQLException {
    logger.info("Update balance player with id {}", id);
    return updatePlayer(getPlayerById(id).setBalance(balance));
  }

  public List<String> getAllValuesAsStrings(Player player) {
    return List.of(player.getPlayerId().toString(), player.getRegistrationDate().toString(),
            player.getStatus().toString(), player.getCountry().toString(),
            player.getBalance().toString(), player.getCurrency());
  }
}

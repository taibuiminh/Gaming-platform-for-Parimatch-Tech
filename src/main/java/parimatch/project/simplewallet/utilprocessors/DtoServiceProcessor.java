package parimatch.project.simplewallet.utilprocessors;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;
import parimatch.project.simplewallet.DB.dto.Game;
import parimatch.project.simplewallet.DB.dto.GameRound;
import parimatch.project.simplewallet.DB.dto.Player;
import parimatch.project.simplewallet.DB.dto.Transaction;
import parimatch.project.simplewallet.DB.services.GameRoundService;
import parimatch.project.simplewallet.DB.services.GameService;
import parimatch.project.simplewallet.DB.services.PlayerService;
import parimatch.project.simplewallet.DB.services.TransactionService;
import parimatch.project.simplewallet.communicationmodels.TransactionRequest;
import parimatch.project.simplewallet.communicationmodels.TransactionResponse;
import parimatch.project.simplewallet.exceptions.*;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class DtoServiceProcessor {

  private static final TransactionService transactionService = new TransactionService();
  private static final PlayerService playerService = new PlayerService();
  private static final GameRoundService gameRoundService = new GameRoundService();
  private static final GameService gameService = new GameService();

  private static final Logger logger = LoggerFactory.getLogger(DtoServiceProcessor.class);

  public static Player retrievePlayerFromDB(int playerId) throws SQLException {
    logger.info("Getting player from DB using following playerId: {}", playerId);
    Player player = playerService.getPlayerById(playerId);
    if (player == null) {
      throw new WrongPlayerIdException();
    }
    checkIfPlayerIsBlocked(player);
    return player;
  }

  private static void checkIfPlayerIsBlocked(Player player) throws PlayerBlockedException {
    if (player.getStatus() != 1) {
      throw new PlayerBlockedException();
    }
  }

  public static void updatePlayerBalanceAndInsertNewTransaction(
          TransactionRequest transactionRequest,
          TransactionResponse transactionResponse,
          String transactionType) throws SQLException {
    logger.info("Update player Balance");

    if (playerService.updatePlayerBalance(
            Integer.parseInt(transactionResponse.getPlayerId()),
            transactionResponse.getBalance()) == null) {
      throw new SQLClientInfoException();
    }

    logger.info("Inserting new transaction...");
    transactionService.insertTransaction(
            new Transaction(
                    transactionResponse.retrieveOriginalTxIdAsInteger(),
                    transactionRequest.retrieveRoundIdAsInteger(),
                    transactionRequest.getAmount().intValue(),
                    transactionType,
                    System.currentTimeMillis(),
                    transactionResponse.getWalletTxId(),
                    transactionRequest.getCurrency()
            )
    );
  }

  public static String generateWalletTxIdHash(
          TransactionRequest transactionRequest, Player player) {
    logger.info("Getting list of values from transaction request");
    List<String> allValuesAsStrings = transactionRequest.getAllValuesAsStrings();
    allValuesAsStrings.addAll(playerService.getAllValuesAsStrings(player));

    logger.info("Sorting transaction values ");
    String allValuesSortedAndConcatenated =
            allValuesAsStrings
                    .stream()
                    .sorted()
                    .collect(Collectors.joining());
    allValuesSortedAndConcatenated += Config.getSetting("SECRET_KEY");
    logger.info("Generating HASH value from the following concatenated string values: {}",
            allValuesSortedAndConcatenated);

    String walletTxIdHash = DigestUtils.md5Hex(allValuesSortedAndConcatenated);
    logger.info("Generated HASH value for transaction: {}", walletTxIdHash);
    return walletTxIdHash;
  }

  public static boolean transactionHasNotOccurred(TransactionResponse walletTxId) throws SQLException {
    boolean exists = transactionService.transactionWalletTxIdExistsInDatabase(walletTxId);
    return !exists;
  }

  public static boolean playerHasEnoughMoneyForBet(Integer playerId, Double bet) throws InsufficientFundsException,
          SQLException {
    if (playerService.getPlayersBalance(playerId) >= bet) {
      return true;
    } else {
      throw new InsufficientFundsException();
    }
  }

  public static boolean roundIsNotOver(Integer gameRoundId) throws RoundIsOverException, SQLException {
    GameRound gameRound = gameRoundService.getGameRoundById(gameRoundId);
    GameRound reviewedGameRound = gameRoundService.updateIfRoundShouldBeEnded(gameRound);
    gameRoundService.updateGameRound(reviewedGameRound);
    if (reviewedGameRound.getFinished()) {
      throw new RoundIsOverException();
    }
    return true;
  }

  public static void insertNewRound(TransactionRequest transactionRequest) throws SQLException {
    gameRoundService.insertGameRound(new GameRound(
            transactionRequest.retrieveRoundIdAsInteger(),
            transactionRequest.retrievePlayerIdAsInteger(),
            transactionRequest.getGameId(),
            new Timestamp(System.currentTimeMillis()),
            null,
            false
    ));
  }

  public static void insertNewGame(TransactionRequest transactionRequest) throws SQLException {
    if (gameService.getGameById(transactionRequest.getGameId()) == null) {
      gameService.insertGame(new Game(
              transactionRequest.getGameId(),
              1
      ));
    }
  }

  public static boolean currencyMatchesPlayerBalanceRecords(String playerId, String currency) throws Exception {
    Player player = playerService.getPlayerById(Integer.parseInt(playerId));
    if (player.getCurrency().equals(currency)) {
      return true;
    } else {
      throw new IncorrectCurrenciesException();
    }
  }

  public static boolean amountIsPositive(Double amount) throws Exception {
    if (amount <= 0) {
      throw new NegativeAmountException();
    }
    return true;
  }
}

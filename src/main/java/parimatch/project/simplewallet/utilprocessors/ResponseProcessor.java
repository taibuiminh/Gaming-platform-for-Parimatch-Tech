package parimatch.project.simplewallet.utilprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dto.Player;
import parimatch.project.simplewallet.communicationmodels.PlayerAccountInformationResponse;
import parimatch.project.simplewallet.communicationmodels.TransactionRequest;
import parimatch.project.simplewallet.communicationmodels.TransactionResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ResponseProcessor {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);

  public static TransactionRequest deserializeJSONIncomingTransactionRequest(HttpServletRequest req) throws
          IOException {
    logger.info("Deserializing JSON incoming transaction request...");
    TransactionRequest transactionRequest;
    try (BufferedReader reader = req.getReader()) {
      logger.info("Getting json body from request...");
      String jsonBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
      logger.info("Current json body is: {}", jsonBody);
      transactionRequest = mapper.readValue(jsonBody, TransactionRequest.class);
      logger.info("New transaction request was created: {}", transactionRequest);

    }
    return transactionRequest;
  }

  public static String serializeSimpleWalletTransactionResponse(TransactionResponse transactionResponse)
          throws JsonProcessingException {
    String serializedSimpleWalletTransactionResponse = mapper.writeValueAsString(transactionResponse);
    logger.info("Serialized simple wallet transaction response: {}", serializedSimpleWalletTransactionResponse);
    return serializedSimpleWalletTransactionResponse;
  }

  public static String serializePlayerAccountInformationResponse
          (PlayerAccountInformationResponse playerAccountInformationResponse)
          throws JsonProcessingException {
    String serializedPlayerAccountInformationResponse = mapper.writeValueAsString(playerAccountInformationResponse);
    logger.info("Serialized player account information response: {}", serializedPlayerAccountInformationResponse);
    return serializedPlayerAccountInformationResponse;
  }

  public static TransactionResponse generateResponseFromRequest(TransactionRequest transactionRequest) throws
          SQLException {
    Integer playerId = transactionRequest.retrievePlayerIdAsInteger();
    logger.info("Retrieved Player id from transaction request: {}", playerId);
    Player player = DtoServiceProcessor.retrievePlayerFromDB(playerId);
    TransactionResponse transactionResponse = new TransactionResponse(
            String.valueOf(playerId),
            transactionRequest.getTxId(),
            DtoServiceProcessor.generateWalletTxIdHash(transactionRequest, player),
            player.getBalance(),
            transactionRequest.getCurrency()
    );
    logger.info("Created new Transaction Response: {}", transactionResponse);
    return transactionResponse;
  }

  public static PlayerAccountInformationResponse generatePlayerAccountInformationResponse(Player player) {
    PlayerAccountInformationResponse playerAccountInformationResponse = new PlayerAccountInformationResponse(
            String.valueOf(player.getPlayerId()),
            player.getBalance(),
            player.getCurrency()
    );
    logger.info("Generated new Player account information response: {}", playerAccountInformationResponse);
    return playerAccountInformationResponse;
  }
}

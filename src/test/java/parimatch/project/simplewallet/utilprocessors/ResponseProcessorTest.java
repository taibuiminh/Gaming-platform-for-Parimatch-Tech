package parimatch.project.simplewallet.utilprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.h2.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import parimatch.project.simplewallet.DB.dto.Player;
import parimatch.project.simplewallet.communicationmodels.PlayerAccountInformationResponse;
import parimatch.project.simplewallet.communicationmodels.TransactionRequest;
import parimatch.project.simplewallet.communicationmodels.TransactionResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResponseProcessorTest {

  String jsonWinBodyFromGis = "{\"playerId\": \"1\", \"amount\": 100.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"54321\"} }";
  String jsonResponseWinFromSw = "{\"playerId\":\"1\",\"originalTxId\":\"54321\",\"walletTxId\":null,\"balance\":0.0,\"currency\":\"EUR\"}";
  String jsonResponsePlayerInformation = "{\"playerId\":\"1\",\"balance\":0.0,\"currency\":\"USD\"}";
  Player testPlayer = new Player(1, new Timestamp(0), 1, 1, 0.0, "USD");

  @Mock
  HttpServletRequest request;

  BufferedReader stubBufferedReader = new BufferedReader(
          new InputStreamReader(IOUtils.getInputStream(jsonWinBodyFromGis)));

  @Test
  public void deserializeJSONIncomingTransactionRequest() throws IOException {
    when(request.getReader()).thenReturn(stubBufferedReader);

    TransactionRequest transactionRequest = ResponseProcessor.deserializeJSONIncomingTransactionRequest(request);

    assertEquals("1", transactionRequest.getPlayerId());
    assertEquals(Double.valueOf(100.00), transactionRequest.getAmount());
    assertEquals("EUR", transactionRequest.getCurrency());
    assertEquals("54321", transactionRequest.getTxId());
    assertEquals("2", transactionRequest.getRoundId());
  }

  @Test
  public void serializeSimpleWalletTransactionResponse() throws IOException, SQLException {
    when(request.getReader()).thenReturn(stubBufferedReader);
    TransactionRequest transactionRequest = ResponseProcessor.deserializeJSONIncomingTransactionRequest(request);

    MockedStatic<DtoServiceProcessor> serviceProcessor = Mockito.mockStatic(DtoServiceProcessor.class);
    serviceProcessor.when((MockedStatic.Verification) DtoServiceProcessor.retrievePlayerFromDB(1))
            .thenReturn(testPlayer);

    TransactionResponse transactionResponse = ResponseProcessor.generateResponseFromRequest(transactionRequest);
    serviceProcessor.close();

    String serializedSimpleWalletTransactionResponse =
            ResponseProcessor.serializeSimpleWalletTransactionResponse(transactionResponse);

    assertEquals(jsonResponseWinFromSw, serializedSimpleWalletTransactionResponse);
  }

  @Test
  public void serializePlayerAccountInformationResponse() throws JsonProcessingException {
    PlayerAccountInformationResponse informationResponse =
            ResponseProcessor.generatePlayerAccountInformationResponse(testPlayer);

    String serializedPlayerAccountInformationResponse =
            ResponseProcessor.serializePlayerAccountInformationResponse(informationResponse);

    assertEquals(jsonResponsePlayerInformation, serializedPlayerAccountInformationResponse);
  }

  @Test
  public void generateResponseFromRequest() throws IOException, SQLException {
    when(request.getReader()).thenReturn(stubBufferedReader);
    TransactionRequest transactionRequest = ResponseProcessor.deserializeJSONIncomingTransactionRequest(request);

    MockedStatic<DtoServiceProcessor> serviceProcessor = Mockito.mockStatic(DtoServiceProcessor.class);
    serviceProcessor.when((MockedStatic.Verification) DtoServiceProcessor.retrievePlayerFromDB(1))
            .thenReturn(testPlayer);

    TransactionResponse generatedTransactionResponse = ResponseProcessor.generateResponseFromRequest(
            transactionRequest);
    serviceProcessor.close();

    assertEquals("1", generatedTransactionResponse.getPlayerId());
    assertEquals("54321", generatedTransactionResponse.getOriginalTxId());
    assertNull(generatedTransactionResponse.getWalletTxId());
    assertEquals(Double.valueOf(0.0), generatedTransactionResponse.getBalance());
    assertEquals("EUR", generatedTransactionResponse.getCurrency());
  }

  @Test
  public void generatePlayerAccountInformationResponse() {
    PlayerAccountInformationResponse informationResponse =
            ResponseProcessor.generatePlayerAccountInformationResponse(testPlayer);

    assertEquals(String.valueOf(testPlayer.getPlayerId()), informationResponse.getPlayerId());
    assertEquals(testPlayer.getBalance(), informationResponse.getBalance());
    assertEquals(testPlayer.getCurrency(), informationResponse.getCurrency());
  }
}
package parimatch.project.gameintegrationservice.utilprocessors;

import org.h2.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import parimatch.project.gameintegrationservice.communicationmodels.SuccessfulQueryResponse;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.GameActivity;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.Round;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataParserTest {

  String jsonBetRequest = "{\"playerId\": \"42\", \"amount\": 20.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"4321\"} }";
  String jsonBodyResponseFromSimpleWallet = "{\"playerId\":\"42\",\"originalTxId\":\"4321\",\"walletTxId\":\"c4ca4238a0b923820dcc509a6f75849b\",\"balance\":80.00,\"currency\":\"EUR\"}";
  String expectedJsonBodyResponseFromGameIntegrationService = "{\"status\":\"success\",\"data\":{\"playerId\":\"42\",\"originalTxId\":\"4321\",\"walletTxId\":\"c4ca4238a0b923820dcc509a6f75849b\",\"balance\":80.00,\"currency\":\"EUR\"}}";

  @Mock
  HttpURLConnection conn;
  @Mock
  HttpServletResponse resp;
  InputStream stubInputStream =
          IOUtils.getInputStream(jsonBodyResponseFromSimpleWallet);

  @Test
  public void getAndParseResponse() throws IOException {

    when(conn.getResponseCode()).thenReturn(200);
    when(conn.getInputStream()).thenReturn(stubInputStream);

    String parsedJsonResponse = DataParser.getAndParseResponse(conn, resp);
    assertEquals(jsonBodyResponseFromSimpleWallet, parsedJsonResponse);
  }

  @Test
  public void retrieveValuesFromJSON() {
    List<String> retrievedValues = DataParser.retrieveValuesFromJSON(jsonBetRequest);

    assertEquals(List.of("42", "20.0", "EUR", "fruits", "2", "4321"), retrievedValues);
  }

  @Test
  public void generateSuccessfulResponse() {
    SuccessfulQueryResponse response = new SuccessfulQueryResponse(jsonBodyResponseFromSimpleWallet);

    String generatedSuccessfulResponse = DataParser.generateSuccessfulResponse(response);

    assertEquals(expectedJsonBodyResponseFromGameIntegrationService, generatedSuccessfulResponse);
  }

  @Test
  public void generateGameActivity() {
    GameActivity generatedBet = DataParser.generateGameActivity(jsonBetRequest, "bet");

    assertEquals(Integer.valueOf(4321), generatedBet.getTxId());
    assertEquals("bet", generatedBet.getTypeTransaction());
  }

  @Test
  public void generateRound() {
    Round generateRound = DataParser.generateRound(jsonBetRequest);

    assertEquals(Integer.valueOf(2), generateRound.getRoundId());
    assertEquals(Integer.valueOf(42), generateRound.getPlayerId());
    assertEquals("fruits", generateRound.getGameId());
  }
}
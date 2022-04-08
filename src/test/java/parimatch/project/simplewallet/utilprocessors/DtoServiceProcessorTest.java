package parimatch.project.simplewallet.utilprocessors;

import org.h2.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import parimatch.project.simplewallet.DB.dto.Player;
import parimatch.project.simplewallet.communicationmodels.TransactionRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DtoServiceProcessorTest {

  String jsonBetBodyFromGis = "{\"playerId\": \"42\", \"amount\": 20.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"4321\"} }";

  @Mock
  HttpServletRequest request;

  InputStream stubInputStream =
          IOUtils.getInputStream(jsonBetBodyFromGis);

  @Test
  public void generateWalletTxIdHash() throws IOException {

    when(request.getReader()).thenReturn(new BufferedReader(new InputStreamReader(stubInputStream)));

    TransactionRequest testTransactionRequest = ResponseProcessor.deserializeJSONIncomingTransactionRequest(request);
    Player testPlayer = new Player(1, new Timestamp(0), 1, 1, 0.0, "USD");

    String generatedWalletTxIdHash = DtoServiceProcessor.generateWalletTxIdHash(testTransactionRequest, testPlayer);
    assertEquals("92463af86622447e7b049408ee1926d5", generatedWalletTxIdHash);
  }
}
package parimatch.project.gameintegrationservice.utilprocessors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestHashValidatorTest {

  @Mock
  HttpServletRequest httpServletRequest;

  String jsonBodyWinRequest = "{\"playerId\": \"42\", \"amount\": 100.00, \"currency\": \"EUR\", \"transaction\": {\"gameId\": \"fruits\", \"roundId\": \"2\", \"txId\": \"54321\"} }";

  @Test
  public void getRequestHashSignIsCorrect() throws Exception {
    when(httpServletRequest.getHeader("request-hash-sign")).thenReturn("477f8f56ae968063ad280d7c693fd529");
    assertTrue(RequestHashValidator.getRequestHashSignIsCorrect(httpServletRequest, "42"));
  }

  @Test
  public void postRequestHashSignIsCorrect() throws Exception {
    when(httpServletRequest.getHeader("request-hash-sign")).thenReturn("63cab9230d7b238e9694272b61f7c148");
    assertTrue(RequestHashValidator.postRequestHashSignIsCorrect(httpServletRequest, jsonBodyWinRequest));
  }
}
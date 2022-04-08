package parimatch.project.gameintegrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.gameintegrationservice.communicationmodels.SuccessfulQueryResponse;
import parimatch.project.gameintegrationservice.exceptions.BetAlreadySettledException;
import parimatch.project.gameintegrationservice.exceptions.SecurityKeyMismatchException;
import parimatch.project.gameintegrationservice.utilprocessors.DataParser;
import parimatch.project.gameintegrationservice.utilprocessors.GameFlowValidator;
import parimatch.project.gameintegrationservice.utilprocessors.RequestHashValidator;
import parimatch.project.gameintegrationservice.utilprocessors.Router;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;


@WebServlet(name = "game-bet", urlPatterns = "/game/bet")
public class BetServlet extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(BetServlet.class);

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    logger.info("[/game/bet] POST request");
    logger.info("Getting json body...");

    String jsonBodyStr = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    logger.info("Json body: {}", jsonBodyStr);
    logger.info("MD5 hash value for current Json body is: {}",
            RequestHashValidator.generateMD5Hash(DataParser.retrieveValuesFromJSON(jsonBodyStr)));

    logger.info("Checking HASH SIGN for correctness...");
    String responseBody = "";

    try {
      if (RequestHashValidator.postRequestHashSignIsCorrect(req, jsonBodyStr)
              && GameFlowValidator.addBetToHistory(jsonBodyStr)) {
        logger.info("Redirecting request to /db/credit ...");
        HttpURLConnection conn = Router.sendPostRequestToSimpleWallet(jsonBodyStr, "/db/credit");
        String simpleWalletResponse = DataParser.getAndParseResponse(conn, resp);

        logger.info("Creating successful response...");
        SuccessfulQueryResponse successfulResponse = new SuccessfulQueryResponse(simpleWalletResponse);
        logger.info("Parsing data...");
        String gameIntegrationServiceResponse = DataParser.generateSuccessfulResponse(successfulResponse);
        logger.info("Parsed data looks like: {}", gameIntegrationServiceResponse);
        logger.info("Printing response in output...");
        resp.getWriter().write(gameIntegrationServiceResponse);
      }
    } catch (SecurityKeyMismatchException e) {
      resp.setStatus(422);
      responseBody = DataParser.generateBadResponse(810);
    } catch (BetAlreadySettledException e) {
      resp.setStatus(422);
      responseBody = DataParser.generateBadResponse(833);
    } catch (Exception e) {
      resp.setStatus(422);
      responseBody = DataParser.generateBadResponse(801);
    } finally {
      try (PrintWriter writer = resp.getWriter()) {
        logger.info("Parsed data looks like: {}", responseBody);
        logger.info("Printing response in output...");
        writer.write(responseBody);
      }
    }
  }
}

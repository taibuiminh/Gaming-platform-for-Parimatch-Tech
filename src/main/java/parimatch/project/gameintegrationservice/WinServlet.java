package parimatch.project.gameintegrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.gameintegrationservice.exceptions.BetNotFoundException;
import parimatch.project.gameintegrationservice.exceptions.SameTxIdException;
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

@WebServlet(name = "game-win", urlPatterns = "/game/win")
public class WinServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(WinServlet.class);

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    logger.info("[/game/win] POST request");
    logger.info("Getting json body...");

    String jsonBodyStr = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    logger.info("Json body: {}", jsonBodyStr);
    logger.info("MD5 hash value for current Json body is: {}",
            RequestHashValidator.generateMD5Hash(DataParser.retrieveValuesFromJSON(jsonBodyStr)));
    String responseBody = "";
    try {
      logger.info("Checking HASH SIGN for correctness...");
      if (RequestHashValidator.postRequestHashSignIsCorrect(req, jsonBodyStr)) {
        logger.info("Checking if bet was placed for this round before win...");
        if (GameFlowValidator.checkWin(jsonBodyStr)) {
          logger.info("Redirecting request to /db/debit ...");
          HttpURLConnection conn = Router.sendPostRequestToSimpleWallet(jsonBodyStr, "/db/debit");
          logger.info("Parsing data...");
          responseBody = DataParser.getAndParseResponse(conn, resp);
        }
      } else {
        logger.error("ERROR was occurred, HASH SIGN is not correct");
        resp.getWriter().write("ERROR ");
      }
    } catch (SecurityKeyMismatchException e) {
      resp.setStatus(422);
      logger.error("Error: {}", e.getMessage());
      responseBody = DataParser.generateBadResponse(810);
    } catch (BetNotFoundException e) {
      resp.setStatus(422);
      logger.error("Error: {}", e.getMessage());
      responseBody = DataParser.generateBadResponse(840);
    } catch (SameTxIdException e) {
      resp.setStatus(422);
      logger.error("Error: {}", e.getMessage());
      responseBody = DataParser.generateBadResponse(803);
    } catch (Exception e) {
      resp.setStatus(422);
      logger.error("Error: {}", e.getMessage());
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
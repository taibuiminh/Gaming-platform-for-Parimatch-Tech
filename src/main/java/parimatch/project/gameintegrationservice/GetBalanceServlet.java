package parimatch.project.gameintegrationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.gameintegrationservice.exceptions.SecurityKeyMismatchException;
import parimatch.project.gameintegrationservice.utilprocessors.DataParser;
import parimatch.project.gameintegrationservice.utilprocessors.RequestHashValidator;
import parimatch.project.gameintegrationservice.utilprocessors.Router;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

@WebServlet(name = "game-balance", urlPatterns = "/game/balance")
public class GetBalanceServlet extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(GetBalanceServlet.class);

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    logger.info("[/game/balance] GET request");

    String responseBody = "";

    logger.info("Getting player's id as a parameter");
    String playerId = req.getParameter("playerId");

    logger.info("Current player's id is: {}", playerId);

    logger.info("Checking HASH SIGN for correctness...");

    try {
      if (RequestHashValidator.getRequestHashSignIsCorrect(req, playerId)) {
        logger.info("Creating request for special playerId...");
        HttpURLConnection request = Router.createRequest(playerId);
        logger.info("Parsing request...");
        responseBody = DataParser.getAndParseResponse(request, resp);
      }
    } catch (SecurityKeyMismatchException e) {
      resp.setStatus(422);
      responseBody = DataParser.generateBadResponse(810);
    } catch (Exception e) {
      resp.setStatus(422);
      responseBody = DataParser.generateBadResponse(801);
      logger.info("Parsed data looks like: {}", responseBody);
    } finally {
      try (PrintWriter writer = resp.getWriter()) {
        logger.info("Parsed data looks like: {}", responseBody);
        logger.info("Printing response in output...");
        writer.write(responseBody);
      }
    }
  }
}
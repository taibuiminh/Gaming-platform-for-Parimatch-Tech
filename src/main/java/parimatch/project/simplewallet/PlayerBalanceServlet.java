package parimatch.project.simplewallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.DB.dto.Player;
import parimatch.project.simplewallet.communicationmodels.PlayerAccountInformationResponse;
import parimatch.project.simplewallet.exceptions.PlayerBlockedException;
import parimatch.project.simplewallet.exceptions.WrongPlayerIdException;
import parimatch.project.simplewallet.utilprocessors.DtoServiceProcessor;
import parimatch.project.simplewallet.utilprocessors.ResponseProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "db-balance", urlPatterns = "/db/balance")
public class PlayerBalanceServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(PlayerBalanceServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    logger.info("[/db/balance] GET request");

    String responseBody = "";

    try {
      logger.info("Getting player's balance from Data Base using player id...");
      int playerId = Integer.parseInt(req.getParameter("playerId"));

      Player player = DtoServiceProcessor.retrievePlayerFromDB(playerId);

      PlayerAccountInformationResponse playerAccountInformationResponse =
              ResponseProcessor.generatePlayerAccountInformationResponse(player);
      logger.info("Player Account Information is: {}", playerAccountInformationResponse);

      logger.info("Serialize Player Account Information response");
      responseBody = ResponseProcessor.serializePlayerAccountInformationResponse(playerAccountInformationResponse);
      logger.info("Response body is: {}", responseBody);

    } catch (WrongPlayerIdException e) {
      resp.setStatus(820);
      logger.error("Error: {}", e.getMessage());
    } catch (PlayerBlockedException e) {
      resp.setStatus(821);
      logger.error("Error: {}", e.getMessage());
    } catch (Exception e) {
      resp.setStatus(801);
      logger.error("Error: {}", e.getMessage());
    } finally {
      try (PrintWriter writer = resp.getWriter()) {
        logger.info("Printing response in output...");
        writer.write(responseBody);
      }
    }
  }
}


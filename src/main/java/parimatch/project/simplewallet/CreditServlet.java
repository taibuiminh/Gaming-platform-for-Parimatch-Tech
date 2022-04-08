package parimatch.project.simplewallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.simplewallet.communicationmodels.TransactionRequest;
import parimatch.project.simplewallet.communicationmodels.TransactionResponse;
import parimatch.project.simplewallet.exceptions.*;
import parimatch.project.simplewallet.utilprocessors.DtoServiceProcessor;
import parimatch.project.simplewallet.utilprocessors.ResponseProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "db-credit", urlPatterns = "/db/credit")
public class CreditServlet extends HttpServlet {
  private final Logger logger = LoggerFactory.getLogger(CreditServlet.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    logger.info("[/db/credit] POST request");

    String responseBody = "";

    try {
      logger.info("Deserializing Json into a Transaction request");
      TransactionRequest transactionRequest = ResponseProcessor.deserializeJSONIncomingTransactionRequest(req);
      logger.info("Generating Response from Request");
      TransactionResponse transactionResponse = ResponseProcessor.generateResponseFromRequest(transactionRequest);

      logger.info("Checking if transaction was occurred");
      if (DtoServiceProcessor.transactionHasNotOccurred(transactionResponse)
              && DtoServiceProcessor.playerHasEnoughMoneyForBet(Integer.valueOf(transactionRequest.getPlayerId()),
              transactionRequest.getAmount())
              && DtoServiceProcessor.amountIsPositive(transactionRequest.getAmount())
              && DtoServiceProcessor.currencyMatchesPlayerBalanceRecords(transactionRequest.getPlayerId(),
              transactionRequest.getCurrency())) {
        logger.info("Withdraw from the Balance...");
        DtoServiceProcessor.insertNewGame(transactionRequest);
        DtoServiceProcessor.insertNewRound(transactionRequest);
        transactionResponse.withdrawFromBalance(transactionRequest.getAmount());
        logger.info("Updating Player balance and insert new Transaction");
        DtoServiceProcessor.updatePlayerBalanceAndInsertNewTransaction(transactionRequest, transactionResponse,
                "credit");
      }
      logger.info("Serializing Simple wallet transaction response");
      responseBody = ResponseProcessor.serializeSimpleWalletTransactionResponse(transactionResponse);

    } catch (WrongPlayerIdException e) {
      resp.setStatus(820);
      logger.error("Error: {}", e.getMessage());
    } catch (InsufficientFundsException e) {
      resp.setStatus(831);
      logger.error("Error: {}", e.getMessage());
    } catch (PlayerBlockedException e) {
      resp.setStatus(821);
      logger.error("Error: {}", e.getMessage());
    } catch (NegativeAmountException e) {
      resp.setStatus(811);
      logger.error("Error: {}", e.getMessage());
    } catch (IncorrectCurrenciesException e) {
      resp.setStatus(812);
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

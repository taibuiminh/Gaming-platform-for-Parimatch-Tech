package parimatch.project.gameintegrationservice.utilprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.gameintegrationservice.communicationmodels.FailedQueryResponse;
import parimatch.project.gameintegrationservice.communicationmodels.SuccessfulQueryResponse;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.GameActivity;
import parimatch.project.gameintegrationservice.utilprocessors.gameflow.Round;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataParser {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Logger logger = LoggerFactory.getLogger(DataParser.class);

  public static String getAndParseResponse(HttpURLConnection httpURLConnection,
                                           HttpServletResponse resp) throws IOException {
    logger.info("Getting and parsing response from servlet...");
    int responseCode = httpURLConnection.getResponseCode();
    logger.info("Parsed response code is: {}", responseCode);

    String responseString;

    logger.info("Checking page connection response...");
    if (responseCode == 200) {
      StringBuilder responseBuilder = new StringBuilder();

      BufferedReader in = new BufferedReader(new InputStreamReader(
              httpURLConnection.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        responseBuilder.append(inputLine);
      }
      in.close();

      responseString = String.valueOf(responseBuilder);
    } else {
      resp.setStatus(422);
      responseString = generateBadResponse(responseCode);
    }
    return responseString;
  }

  public static String generateBadResponse(int code) throws JsonProcessingException {
    FailedQueryResponse response;
    String generalGisErrorMessage = "Some error occurred in Game Integration Service";
    String generalSwErrorMessage = "Some error occurred in Simple Wallet";
    String generalErrorMessage = "Some server processing error occurred";
    switch (code) {
      case 801:
        response = new FailedQueryResponse(801, generalErrorMessage);
        return mapper.writeValueAsString(response);
      case 802:
        response = new FailedQueryResponse(802, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 803:
        response = new FailedQueryResponse(803, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 811:
        response = new FailedQueryResponse(811, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 812:
        response = new FailedQueryResponse(812, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 810:
        response = new FailedQueryResponse(810, generalGisErrorMessage);
        return mapper.writeValueAsString(response);
      case 820:
        response = new FailedQueryResponse(820, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 821:
        response = new FailedQueryResponse(821, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 831:
        response = new FailedQueryResponse(831, generalSwErrorMessage);
        return mapper.writeValueAsString(response);
      case 833:
        response = new FailedQueryResponse(833, generalGisErrorMessage);
        return mapper.writeValueAsString(response);
      case 840:
        response = new FailedQueryResponse(840, generalGisErrorMessage);
        return mapper.writeValueAsString(response);
      default:
        return "";
    }
  }

  public static List<String> retrieveValuesFromJSON(String jsonBody) {
    logger.info("Getting values from json as a List of strings...");
    List<String> values = new ArrayList<>();
    try {
      JsonNode node = mapper.readTree(jsonBody);
      values.addAll(extractJsonValuesHelper(node.elements()));
    } catch (JsonProcessingException e) {
      logger.error("ERROR was occurred with Json Processing {}", e.getMessage());
      e.printStackTrace();
    }
    logger.info("Retrieved values from json are the following: {}", values);
    return values;
  }

  private static List<String> extractJsonValuesHelper(Iterator<JsonNode> elements) {
    List<String> values = new ArrayList<>();

    while (elements.hasNext()) {
      JsonNode element = elements.next();
      if (element.isContainerNode()) {
        values.addAll(extractJsonValuesHelper(element.elements()));
        continue;
      }
      values.add(element.asText());
    }
    return values;
  }

  public static String generateSuccessfulResponse(SuccessfulQueryResponse successfulResponse) {
    logger.info("Generating successful response...");
    String response = null;
    try {
      response = mapper.writeValueAsString(successfulResponse);
    } catch (JsonProcessingException e) {
      logger.error("ERROR was occurred with Json Processing {}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  public static GameActivity generateGameActivity(String jsonBody, String typeTransaction) {
    logger.info("Generating {} game activity from {}", typeTransaction, jsonBody);
    GameActivity gameActivity = null;
    try {
      gameActivity = mapper.readValue(jsonBody, GameActivity.class);
      logger.info("Setting Transaction type to 'bet'");
      gameActivity.setTypeTransaction("bet");
    } catch (JsonProcessingException e) {
      logger.error("ERROR was occurred with Json Processing {}", e.getMessage());
      e.printStackTrace();
    }
    return gameActivity;
  }

  public static Round generateRound(String jsonBody) {
    logger.info("Generating round from {}", jsonBody);
    Round round = null;
    try {
      round = mapper.readValue(jsonBody, Round.class);
    } catch (JsonProcessingException e) {
      logger.error("ERROR was occurred with Json Processing {}", e.getMessage());
      e.printStackTrace();
    }
    return round;
  }
}

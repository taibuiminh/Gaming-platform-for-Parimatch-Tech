package parimatch.project.gameintegrationservice.utilprocessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Router {

  private static final Logger logger = LoggerFactory.getLogger(Router.class);

  public static HttpURLConnection createRequest(String playerId) throws IOException {
    logger.info("Creating new request for player with id: {}", playerId);
    URL url = new URL(Config.getSetting("URL") + "db/balance?playerId=" + playerId);
    logger.info("New URL was created: {}", url);
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

    logger.info("Setting request method to GET...");
    httpURLConnection.setRequestMethod("GET");
    logger.info("httpURLConnection is connecting...");
    httpURLConnection.connect();
    return httpURLConnection;
  }

  public static HttpURLConnection sendPostRequestToSimpleWallet(String jsonBodyStr, String urlPathDetails) throws
          IOException {
    logger.info("Sending POST request to simple wallet...");
    URL url = new URL(Config.getSetting("URL") + urlPathDetails);
    logger.info("New URL was created: {}", url);
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

    httpURLConnection.setDoOutput(true);
    httpURLConnection.setRequestProperty("Content-Type", "application/json");
    logger.info("Setting request method to POST...");
    httpURLConnection.setRequestMethod("POST");

    try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
      outputStream.write(jsonBodyStr.getBytes());
      logger.info("Cleaning output stream...");
      outputStream.flush();
    }

    logger.info("httpURLConnection is connecting...");
    httpURLConnection.connect();
    return httpURLConnection;
  }
}

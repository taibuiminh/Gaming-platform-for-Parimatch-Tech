package parimatch.project.gameintegrationservice.utilprocessors;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parimatch.project.Config;
import parimatch.project.gameintegrationservice.exceptions.SecurityKeyMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RequestHashValidator {

  private static final Logger logger = LoggerFactory.getLogger(RequestHashValidator.class);

  public static boolean getRequestHashSignIsCorrect(HttpServletRequest req, String parameter)
          throws Exception {
    logger.info("Checking Hash Sign is correct in GET request...");
    String requestHash = req.getHeader("request-hash-sign");
    if (requestHash == null) {
      throw new Exception();
    }
    logger.info("The request-hash-sign is: {}", requestHash);
    String generatedHash = generateMD5Hash(List.of(parameter));
    logger.info("Generated hash value is: {}", generatedHash);

    if (requestHash.equals(generatedHash)) {
      return true;
    } else {
      throw new SecurityKeyMismatchException();
    }
  }

  public static boolean postRequestHashSignIsCorrect(HttpServletRequest req, String jsonBody) throws
          Exception {
    logger.info("Checking Hash Sign is correct in POST request...");
    String requestHash = req.getHeader("request-hash-sign");
    if (requestHash == null) {
      throw new Exception();
    }
    logger.info("The request-hash-sign is: {}", requestHash);
    String generatedHash = generateMD5Hash(DataParser.retrieveValuesFromJSON(jsonBody));
    logger.info("Generated hash value is: {}", generatedHash);
    if (requestHash.equals(generatedHash)) {
      return true;
    } else {
      throw new SecurityKeyMismatchException();
    }

  }

  public static String generateMD5Hash(List<String> values) {
    logger.info("Creating MD5 hash value...");
    StringBuilder allValues = new StringBuilder();
    for (String value : values) {
      allValues.append(value);
    }
    allValues.append(Config.getSetting("SECRET_KEY"));

    String stringValues = allValues.toString();
    logger.info("Creating MD5 hash value from string: {}", stringValues);

    return DigestUtils
            .md5Hex(stringValues);
  }
}

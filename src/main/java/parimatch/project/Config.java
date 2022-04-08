package parimatch.project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

  private static final Properties properties = new Properties();

  static {
    try {

      String resourceName = "config.properties";
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
        properties.load(resourceStream);
      }
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static String getSetting(String key) {
    return properties.getProperty(key);
  }
}
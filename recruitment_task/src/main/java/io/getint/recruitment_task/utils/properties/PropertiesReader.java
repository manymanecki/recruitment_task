package io.getint.recruitment_task.utils.properties;

import io.getint.recruitment_task.utils.logging.CustomLogger;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Level;
import java.io.IOException;

public final class PropertiesReader {
  private PropertiesReader() {
    throw new UnsupportedOperationException(
        "PropertiesReader can't be instanced - use static methods instead");
  }

  // Custom .yaml file parser implementing the custom logging
  public static Map<String, Object> parse(Path path) {
    Yaml yaml = new Yaml();
    try {
      if (!Files.exists(path) || !Files.isReadable(path)) {
        CustomLogger.log(Level.SEVERE, "Failed to parse configuration file - not found");
        System.exit(1000);
      }
      return yaml.load(Files.readString(path));
    } catch (IOException ex) {
      CustomLogger.log(Level.SEVERE, "Failed to read configuration file - IOException");
      System.exit(1000);
    }
    // Should not get here
    CustomLogger.log(
        Level.WARNING, "Try-catch block failed to, both, catch exception and parse the file");
    return null;
  }

  // Properties parser given the Map structure I have decided to use
  public static String get(Map<String, Object> properties, String key) {
    // Dot denominator - personal preference from Spring
    String[] keys = key.split("\\.");
    Object value = properties;

    /**
     * Custom properties parser For the jira.username.test.json.access key: 1. Get 'jira' key 2. Check if
     * previous value (in this case properties map) is a map 3. Continue until we are out of keys
     * (in this case, access is the last ) 4. Value will now hold the proper value and will be
     * returned
     */
    for (String k : keys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(k);
      } else {
        return null;
      }
    }
    return value != null ? value.toString() : null;
  }
}

package io.getint.recruitment_task.utils.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CustomLogger {
  private CustomLogger() {
    throw new UnsupportedOperationException(
        "CustomLogger can't be instanced - use static methods instead");
  }

  private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());

  static {
    ConsoleHandler handler = new ConsoleHandler();
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    logger.addHandler(handler);
    logger.setUseParentHandlers(false);
  }

  public static void log(Level level, String message) {
    logger.log(level, message);
  }

  public static void log(String message) {
    logger.log(Level.INFO, message);
  }
}

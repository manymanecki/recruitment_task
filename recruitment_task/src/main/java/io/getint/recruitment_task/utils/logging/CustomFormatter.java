package io.getint.recruitment_task.utils.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");

  @Override
  public String format(LogRecord log) {
    return "\u001B[33m"
        + dateFormat.format(new Date(log.getMillis()))
        + " - ["
        + log.getLevel().getName()
        + "]: "
        + formatMessage(log)
        + "\u001B[33m"
        + "\n";
  }
}

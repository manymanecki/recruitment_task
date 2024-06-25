package io.getint.recruitment_task.enums;

import java.util.logging.Level;

import static io.getint.recruitment_task.utils.logging.CustomLogger.*;

public enum StatusEnums {
  TO_DO("To Do"),
  IN_PROGRESS("In Progress"),
  DONE("Done");

  private final String value;

  StatusEnums(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static StatusEnums contains(String value) {
    for (StatusEnums status : values()) {
      if (status.value.equalsIgnoreCase(value))
        return status;
    }
    // In case something goes wrong, and we fail to match status to enum, we return default value which is To Do
    log(Level.WARNING, "Failed to match status to an enum - most likely a new status type has appeared");
    return TO_DO;
  }
}

package io.getint.recruitment_task.utils.transition;

import io.getint.recruitment_task.enums.StatusEnums;

public final class StatusTranslator {
  private StatusTranslator() {
    throw new UnsupportedOperationException(
        "StatusTranslator can't be instanced - use static methods instead");
  }

  /**
   * Translation method between ticket's status and the transition property. Transition supports the
   * following IDs:
   *
   * <ul>
   *   <li>11 - Transition to To Do
   *   <li>21 - Transition to In Progress
   *   <li>31 - Transition to Done
   * </ul>
   */
  public static String translate(String name) {
    switch (StatusEnums.contains(name)) {
      case TO_DO -> {
        return "11";
      }
      case IN_PROGRESS -> {
        return "21";
      }
      case DONE -> {
        return "31";
      }
    }
    // In case no match was found, we return default value which is To Do
    return "11";
  }
}

package io.getint.recruitment_task.enums;

public enum RequestEnums {
  GET_ISSUE("/rest/api/3/search?jql=project="),
  GET_COMMENT("/rest/api/3/issue/"),
  POST_ISSUE("/rest/api/3/issue/"),
  POST_COMMENT("/rest/api/3/issue/");

  private final String value;

  RequestEnums(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}

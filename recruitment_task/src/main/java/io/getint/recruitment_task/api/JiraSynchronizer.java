package io.getint.recruitment_task.api;

import io.getint.recruitment_task.dto.comment.CommentDTO;
import io.getint.recruitment_task.dto.issue.*;
import io.getint.recruitment_task.enums.RequestEnums;
import io.getint.recruitment_task.utils.parser.JiraParser;
import io.getint.recruitment_task.utils.transition.StatusTranslator;

import static io.getint.recruitment_task.utils.logging.CustomLogger.log;
import static io.getint.recruitment_task.utils.properties.PropertiesReader.*;
import static io.getint.recruitment_task.utils.http.HttpConnection.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JiraSynchronizer {
  private static final Path PATH = Path.of("recruitment_task/src/main/resources/application.yaml");
  private static final Map<String, Object> PROPERTIES = Objects.requireNonNull(parse(PATH));
  private static final String PROJECT_URL = get(PROPERTIES, "jira.project.url");
  private static final String PROJECT_SOURCE = get(PROPERTIES, "jira.project.source");
  private static final String PROJECT_DESTINATION = get(PROPERTIES, "jira.project.destination");
  private static final String AUTHENTICATION =
      Base64.getEncoder()
          .encodeToString(
              (get(PROPERTIES, "jira.username") + ":" + get(PROPERTIES, "jira.token"))
                  .getBytes(StandardCharsets.UTF_8));

  public static void main(String[] args) {
    moveTasksToOtherProject();
  }

  /**
   * Search for 5 tickets in one project, and move them to the other project Jirawithin same Jira
   * instance. When moving tickets, please move following fields: - summary (title) - description -
   * priority Bonus points for syncing comments.
   */
  public static void moveTasksToOtherProject() {
    log(
        "Moving tasks from project: ["
            + PROJECT_SOURCE
            + "] to project: ["
            + PROJECT_DESTINATION
            + "]");
    // 1. Fetch and parse the JSON into the DTO
    List<IssueDTO> sourceProjectIssues = getIssues();

    // 2. For each issue, fetch and parse comments
    Map<String, List<CommentDTO>> sourceProjectComments = getComments(sourceProjectIssues);

    // 3. Post list of issues to the destination project and compare old IDs with new ones
    Map<String, String> idsMap = postIssues(sourceProjectIssues);

    // 4. Post comments to the new issues
    postComments(idsMap, sourceProjectComments);
  }

  private static List<IssueDTO> getIssues() {
    // Was not sure if we should literally search for 5 tickets (then we should call
    // /rest/api/3/issue/{issueIdOrKey} endpoint), or limit the results to 5. Decided to go with the
    // latter, but in any case changing the endpoint and adjusting the logic would take few minutes
    // tops.
    String json =
        execute(
            create(
                AUTHENTICATION,
                PROJECT_URL,
                RequestEnums.GET_ISSUE,
                List.of(PROJECT_SOURCE, "&maxResults=5")));

    return JiraParser.parseToList(json, IssueDTO.class);
  }

  private static Map<String, List<CommentDTO>> getComments(List<IssueDTO> sourceProjectIssues) {
    // Stream the issues
    return sourceProjectIssues.stream()
        .collect(
            // Create map with issue ID as a key
            Collectors.toMap(
                IssueDTO::id,
                issue -> {
                  // Fetch comments JSON for the given issue
                  // No bulk endpoint for that, have to go one by one:(
                  String json =
                      execute(
                          create(
                              AUTHENTICATION,
                              PROJECT_URL,
                              RequestEnums.GET_COMMENT,
                              List.of(issue.id(), "/comment")));
                  // Parse the JSON into DTO
                  return JiraParser.parseToList(json, CommentDTO.class);
                }));
  }

  private static Map<String, String> postIssues(List<IssueDTO> sourceProjectIssues) {
    // Create batch update request body from the IssueDTO that came from source project earlier
    String requestBody =
        JiraParser.parseToString(
            new IssueUpdatesDTO(
                // Modify the IssueDTO that came from the source destination
                sourceProjectIssues.stream()
                    // Change the project
                    .peek(issue -> issue.fields().project().setKey(PROJECT_DESTINATION))
                    // Remap the DTO
                    .map(
                        issue ->
                            new IssueRequestDTO(
                                issue.id(),
                                // Need to create new FieldDTO here as JIRA does not like having
                                // status in the DTO
                                new FieldDTO(
                                    issue.fields().summary(),
                                    issue.fields().description(),
                                    issue.fields().project(),
                                    issue.fields().issuetype(),
                                    issue.fields().priority(),
                                    null),
                                // Initialize transition based on status
                                new TransitionDTO(
                                    StatusTranslator.translate(issue.fields().status().name()))))
                    .toList()));

    // Execute the update (basically duplicates issues from source to destination)
    String result =
        execute(
            create(
                AUTHENTICATION,
                PROJECT_URL,
                RequestEnums.POST_ISSUE,
                List.of("bulk"),
                requestBody));

    return mapOldIdsToNewIds(sourceProjectIssues, result);
  }

  private static void postComments(
      Map<String, String> issueIdMapping, Map<String, List<CommentDTO>> requestComments) {
    // Stream the entry set of comments (old ids)
    requestComments.entrySet().stream()
        .flatMap(
            entry ->
                entry.getValue().stream()
                    // Map old comments to new IDs
                    .map(
                        comment ->
                            new AbstractMap.SimpleEntry<>(
                                issueIdMapping.get(entry.getKey()), comment)))
        // For each entry in the flattened stream
        .forEach(
            entry -> {
              // Convert to a JSON...
              String requestBody = JiraParser.parseToString(entry.getValue());
              // ...and execute
              execute(
                  create(
                      AUTHENTICATION,
                      PROJECT_URL,
                      RequestEnums.POST_COMMENT,
                      List.of(entry.getKey(), "/comment"),
                      requestBody));
            });
  }

  private static Map<String, String> mapOldIdsToNewIds(
      List<IssueDTO> sourceProjectIssues, String result) {
    // Get newly created DTOs
    List<IssueDTO> newIssues = JiraParser.parseToList(result, IssueDTO.class);

    // Map old IDs to new ones
    Map<String, String> issueIdMapping = new HashMap<>();
    for (int i = 0; i < sourceProjectIssues.size(); i++) {
      issueIdMapping.put(sourceProjectIssues.get(i).id(), newIssues.get(i).id());
    }
    return issueIdMapping;
  }
}

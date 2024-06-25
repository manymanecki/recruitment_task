package io.getint.recruitment_task.dto.issue;

public record FieldDTO(
    String summary, DescriptionDTO description, ProjectDTO project, IssueTypeDTO issuetype, PriorityDTO priority, StatusDTO status) {}

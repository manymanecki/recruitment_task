package io.getint.recruitment_task.dto.issue;

public record IssueRequestDTO(
        String id,
        FieldDTO fields,
        TransitionDTO transition
) {}

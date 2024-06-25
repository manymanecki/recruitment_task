package io.getint.recruitment_task.dto.issue;

import java.util.List;

public record IssueUpdatesDTO(
        List<IssueRequestDTO> issueUpdates
) {}

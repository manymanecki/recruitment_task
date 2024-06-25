package io.getint.recruitment_task.dto.comment;

public record UpdateAuthorDTO(
        String accountId,
        Boolean active,
        String displayName,
        String self
) {}

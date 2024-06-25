package io.getint.recruitment_task.dto.comment;

public record AuthorDTO(
        String accountId,
        Boolean active,
        String displayName,
        String self
) {}

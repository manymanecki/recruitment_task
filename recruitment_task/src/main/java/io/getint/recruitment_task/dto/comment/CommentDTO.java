package io.getint.recruitment_task.dto.comment;

public record CommentDTO(
        AuthorDTO author,
        BodyDTO body,
        String created,
        String id,
        String self,
        UpdateAuthorDTO updateAuthor,
        String updated
) {}

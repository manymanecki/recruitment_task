package io.getint.recruitment_task.dto.comment;

import io.getint.recruitment_task.dto.content.OuterContentDTO;

import java.util.List;

public record BodyDTO(
        String type,
        Integer version,
        List<OuterContentDTO> content
) {}

package io.getint.recruitment_task.dto.issue;

import io.getint.recruitment_task.dto.content.OuterContentDTO;

import java.util.List;

public record DescriptionDTO(
        String type,
        Integer version,
        List<OuterContentDTO> content
) {}

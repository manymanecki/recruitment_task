package io.getint.recruitment_task.dto.content;

import java.util.List;

public record OuterContentDTO(
        String type,
        List<InnerContentDTO> content
) {}

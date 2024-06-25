package io.getint.recruitment_task.dto.issue;

public record StatusCategoryDTO(
        String self,
        Integer id,
        String key,
        String colorName,
        String name
) {}

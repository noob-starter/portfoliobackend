package com.backend.portfolio.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPointRequestInsert {

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Project ID is required")
    private Long projectId;
}


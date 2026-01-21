package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.ProjectPoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPointResponse {

    private Long id;
    private String content;

    public static ProjectPointResponse fromProjectPoint(ProjectPoint projectPoint) {
        return ProjectPointResponse.builder()
                .id(projectPoint.getId())
                .content(projectPoint.getContent())
                .build();
    }
}


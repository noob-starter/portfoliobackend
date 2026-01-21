package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.ExperiencePoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperiencePointResponse {

    private Long id;
    private String content;

    public static ExperiencePointResponse fromExperiencePoint(ExperiencePoint experiencePoint) {
        return ExperiencePointResponse.builder()
                .id(experiencePoint.getId())
                .content(experiencePoint.getContent())
                .build();
    }
}


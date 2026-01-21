package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.states.enums.Proficiency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyResponse {

    private Long id;
    private String name;
    private String category;
    private String type;
    private Proficiency proficiency;
    private String banner;
    private String github;

    public static TechnologyResponse fromTechnology(Technology technology) {
        return TechnologyResponse.builder()
                .id(technology.getId())
                .name(technology.getName())
                .category(technology.getCategory())
                .type(technology.getType())
                .proficiency(technology.getProficiency())
                .banner(technology.getBanner())
                .github(technology.getGithub())
                .build();
    }
}


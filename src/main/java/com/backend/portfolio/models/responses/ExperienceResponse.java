package com.backend.portfolio.models.responses;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.backend.portfolio.models.entities.Experience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {

    private Long id;
    private String company;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String url;
    private String banner;
    private String github;
    private Set<Map<String, String>> technologies;
    private Set<Map<String, String>> experiencePoints;

    public static ExperienceResponse fromExperience(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .company(experience.getCompany())
                .position(experience.getPosition())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .location(experience.getLocation())
                .url(experience.getUrl())
                .banner(experience.getBanner())
                .github(experience.getGithub())
                .technologies(experience.getTechnologies() != null ?
                        experience.getTechnologies().stream()
                                .map(tech -> {
                                    Map<String, String> techMap = new LinkedHashMap<>();
                                    techMap.put("name", tech.getName());
                                    return techMap;
                                })
                                .collect(Collectors.toSet()) : Set.of())
                .experiencePoints(experience.getExperiencePoints() != null ?
                        experience.getExperiencePoints().stream()
                                .map(point -> {
                                    Map<String, String> pointMap = new LinkedHashMap<>();
                                    pointMap.put("content", point.getContent());
                                    return pointMap;
                                })
                                .collect(Collectors.toSet()) : Set.of())
                .build();
    }
}


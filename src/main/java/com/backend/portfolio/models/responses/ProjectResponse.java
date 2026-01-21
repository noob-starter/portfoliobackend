package com.backend.portfolio.models.responses;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.backend.portfolio.models.entities.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String url;
    private String banner;
    private String github;
    private Set<Map<String, String>> technologies;
    private Set<Map<String, String>> projectPoints;

    public static ProjectResponse fromProject(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .url(project.getUrl())
                .banner(project.getBanner())
                .github(project.getGithub())
                .technologies(project.getTechnologies() != null ?
                        project.getTechnologies().stream()
                                .map(tech -> {
                                    Map<String, String> techMap = new LinkedHashMap<>();
                                    techMap.put("name", tech.getName());
                                    return techMap;
                                })
                                .collect(Collectors.toSet()) : Set.of())
                .projectPoints(project.getProjectPoints() != null ?
                        project.getProjectPoints().stream()
                                .map(point -> {
                                    Map<String, String> pointMap = new LinkedHashMap<>();
                                    pointMap.put("content", point.getContent());
                                    return pointMap;
                                })
                                .collect(Collectors.toSet()) : Set.of())
                .build();
    }
}


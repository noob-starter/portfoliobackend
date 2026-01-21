package com.backend.portfolio.models.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.backend.portfolio.models.entities.Education;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationResponse {

    private Long id;
    private String degree;
    private String institution;
    private String field;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal percentage;
    private String description;
    private String url;
    private String banner;
    private String github;

    public static EducationResponse fromEducation(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .degree(education.getDegree())
                .institution(education.getInstitution())
                .field(education.getField())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .percentage(education.getPercentage())
                .description(education.getDescription())
                .url(education.getUrl())
                .banner(education.getBanner())
                .github(education.getGithub())
                .build();
    }
}


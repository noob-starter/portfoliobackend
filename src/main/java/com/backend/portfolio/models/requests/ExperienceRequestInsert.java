package com.backend.portfolio.models.requests;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequestInsert {

    @Size(max = 255, message = "Company must not exceed 255 characters")
    private String company;

    @Size(max = 255, message = "Position must not exceed 255 characters")
    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    @Size(max = 512, message = "Github must not exceed 512 characters")
    private String github;

    @NotNull(message = "Profile ID is required")
    private Long profileId;

    private Set<Long> technologyIds;

    private Set<String> experiencePoints;
}


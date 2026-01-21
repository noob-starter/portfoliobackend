package com.backend.portfolio.models.updates;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequestUpdate {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private LocalDate dateAchieved;

    @Size(max = 255, message = "Issuer must not exceed 255 characters")
    private String issuer;

    private String description;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    @Size(max = 512, message = "Github must not exceed 512 characters")
    private String github;

    private Long profileId;
}


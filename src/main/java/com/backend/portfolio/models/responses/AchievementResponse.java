package com.backend.portfolio.models.responses;

import java.time.LocalDate;

import com.backend.portfolio.models.entities.Achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {

    private Long id;
    private String name;
    private LocalDate dateAchieved;
    private String issuer;
    private String description;
    private String url;
    private String banner;
    private String github;

    public static AchievementResponse fromAchievement(Achievement achievement) {
        return AchievementResponse.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .dateAchieved(achievement.getDateAchieved())
                .issuer(achievement.getIssuer())
                .description(achievement.getDescription())
                .url(achievement.getUrl())
                .banner(achievement.getBanner())
                .github(achievement.getGithub())
                .build();
    }
}


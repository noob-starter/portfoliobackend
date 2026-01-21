package com.backend.portfolio.models.updates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperiencePointRequestUpdate {

    private String content;

    private Long experienceId;
}


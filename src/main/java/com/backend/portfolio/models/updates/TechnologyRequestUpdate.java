package com.backend.portfolio.models.updates;

import com.backend.portfolio.models.states.enums.Proficiency;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyRequestUpdate {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Category must not exceed 255 characters")
    private String category;

    @Size(max = 16, message = "Type must not exceed 16 characters")
    private String type;

    private Proficiency proficiency;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    @Size(max = 512, message = "Github must not exceed 512 characters")
    private String github;
}


package com.backend.portfolio.models.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class EducationRequestInsert {

    @Size(max = 255, message = "Degree must not exceed 255 characters")
    private String degree;

    @Size(max = 255, message = "Institution must not exceed 255 characters")
    private String institution;

    @Size(max = 255, message = "Field must not exceed 255 characters")
    private String field;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal percentage;

    private String description;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    @Size(max = 512, message = "Github must not exceed 512 characters")
    private String github;

    @NotNull(message = "Profile ID is required")
    private Long profileId;
}


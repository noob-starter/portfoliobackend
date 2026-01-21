package com.backend.portfolio.models.requests;

import jakarta.validation.constraints.NotBlank;
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
public class ContactRequestInsert {

    @NotBlank(message = "Platform is required")
    @Size(max = 255, message = "Platform must not exceed 255 characters")
    private String platform;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;

    private String description;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    @NotNull(message = "Profile ID is required")
    private Long profileId;
}


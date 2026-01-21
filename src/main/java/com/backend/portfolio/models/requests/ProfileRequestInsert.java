package com.backend.portfolio.models.requests;

import com.backend.portfolio.models.states.enums.Sex;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestInsert {

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String fname;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lname;

    private Sex sex;

    private String bio;

    @Size(max = 512, message = "Banner must not exceed 512 characters")
    private String banner;

    private String intro;

    private String contour;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;
}


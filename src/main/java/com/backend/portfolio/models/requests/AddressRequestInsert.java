package com.backend.portfolio.models.requests;

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
public class AddressRequestInsert {

    @Size(max = 255, message = "Street must not exceed 255 characters")
    private String street;

    @Size(max = 255, message = "Landmark must not exceed 255 characters")
    private String landmark;

    @Size(max = 64, message = "City must not exceed 64 characters")
    private String city;

    @Size(max = 64, message = "State must not exceed 64 characters")
    private String state;

    @Size(max = 64, message = "Country must not exceed 64 characters")
    private String country;

    private Integer pincode;

    @Size(max = 64, message = "Type must not exceed 64 characters")
    private String type;

    @Size(max = 16, message = "Phone must not exceed 16 characters")
    private String phone;

    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Size(max = 255, message = "URL must not exceed 255 characters")
    private String url;

    @NotNull(message = "Profile ID is required")
    private Long profileId;
}


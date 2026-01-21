package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Inquire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquireResponse {

    private Long id;
    private String name;
    private String email;
    private String message;

    public static InquireResponse fromInquire(Inquire inquire) {
        return InquireResponse.builder()
                .id(inquire.getId())
                .name(inquire.getName())
                .email(inquire.getEmail())
                .message(inquire.getMessage())
                .build();
    }
}


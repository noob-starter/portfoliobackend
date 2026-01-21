package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponse {

    private Long id;
    private String question;
    private String answer;

    public static FaqResponse fromFaq(Faq faq) {
        return FaqResponse.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }
}


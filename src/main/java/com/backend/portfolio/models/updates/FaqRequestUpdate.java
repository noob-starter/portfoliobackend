package com.backend.portfolio.models.updates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqRequestUpdate {

    private String question;
    private String answer;
    private Long profileId;
}


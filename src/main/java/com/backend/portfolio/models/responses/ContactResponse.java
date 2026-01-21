package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {

    private Long id;
    private String platform;
    private String url;
    private String description;
    private String banner;

    public static ContactResponse fromContact(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .platform(contact.getPlatform())
                .url(contact.getUrl())
                .description(contact.getDescription())
                .banner(contact.getBanner())
                .build();
    }
}


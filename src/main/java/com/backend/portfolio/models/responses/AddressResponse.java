package com.backend.portfolio.models.responses;

import com.backend.portfolio.models.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private Integer pincode;
    private String type;
    private String phone;
    private String email;
    private String url;

    public static AddressResponse fromAddress(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .type(address.getType())
                .phone(address.getPhone())
                .email(address.getEmail())
                .url(address.getUrl())
                .build();
    }
}


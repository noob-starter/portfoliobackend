package com.backend.portfolio.models.states.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sex {
    MALE("Male"),
    FEMALE("Female"),
    TRANSGENDER("Transgender"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    @JsonValue
    private final String sex;

    public static Sex fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Sex sex : Sex.values()) {
            if (sex.getSex().equals(value)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Unknown sex value: " + value);
    }

    @Converter(autoApply = true)
    public static class SexConverter implements AttributeConverter<Sex, String> {

        @Override
        public String convertToDatabaseColumn(Sex attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getSex();
        }

        @Override
        public Sex convertToEntityAttribute(String dbData) {
            return Sex.fromValue(dbData);
        }
    }
}


package com.backend.portfolio.models.states.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Proficiency {
    EXPERT("Expert"),
    ADVANCED("Advanced"),
    INTERMEDIATE("Intermediate"),
    BEGINNER("Beginner");

    @JsonValue
    private final String proficiency;

    public static Proficiency fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Proficiency proficiency : Proficiency.values()) {
            if (proficiency.getProficiency().equals(value)) {
                return proficiency;
            }
        }
        throw new IllegalArgumentException("Unknown proficiency value: " + value);
    }

    @Converter(autoApply = true)
    public static class ProficiencyConverter implements AttributeConverter<Proficiency, String> {

        @Override
        public String convertToDatabaseColumn(Proficiency attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getProficiency();
        }

        @Override
        public Proficiency convertToEntityAttribute(String dbData) {
            return Proficiency.fromValue(dbData);
        }
    }
}


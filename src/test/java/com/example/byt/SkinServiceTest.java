package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SkinServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validSkinServiceShouldHaveNoViolations() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "Hydration");
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(), "Expected no violations for valid SkinService, but got: " + violations);
    }

    @Test
    void blankPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "   ");
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for blank 'purpose', but got: " + violations);
    }

    @Test
    void nullPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, null);
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for null 'purpose', but got: " + violations);
    }

    private boolean containsViolationFor(Set<ConstraintViolation<SkinService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }


}

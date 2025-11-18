package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validProvidedServiceHasNoViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(4)
                .comment("Great service")
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid ProvidedService");
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void providedServiceWithoutOptionalFieldsIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time).build();
        assertNotNull(service);
        assertNull(service.getRating());
        assertNull(service.getComment());
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for ProvidedService without optional fields");
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void ratingIsNullWhenNotProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time).build();
        assertNull(service.getRating());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void ratingIsSetWhenProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(4)
                .build();
        assertEquals(4, service.getRating());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void commentIsNullWhenNotProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time).build();
        assertNull(service.getComment());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void commentIsSetWhenProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .comment("Good")
                .build();

        assertEquals("Good", service.getComment());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void commentBlankStringIsConvertedToNull() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .comment("   ")
                .build();
        assertNull(service.getComment());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void timeNullProducesValidationError() {
        ProvidedService service = new ProvidedService.Builder(null).build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "time"),
                "Expected violation for 'time' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(service),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingWithinRangeIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(3)
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Rating within valid range should have no violations");
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void ratingAboveMaxProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(6)
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "rating"),
                "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(service),
                "Invalid ProvidedService should NOT be added to extent");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<ProvidedService>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
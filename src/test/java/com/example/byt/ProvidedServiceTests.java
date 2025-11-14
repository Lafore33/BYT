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
        assertTrue(violations.isEmpty());
    }

    @Test
    void providedServiceWithoutOptionalFieldsIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time).build();

        assertNotNull(service);
        assertNull(service.getRating());
        assertNull(service.getComment());

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty());
    }

    @Test
    void ratingIsNullWhenNotProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time).build();

        assertNull(service.getRating());
    }

    @Test
    void ratingIsSetWhenProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time)
                .rating(4)
                .build();

        assertEquals(4, service.getRating());
    }

    @Test
    void commentIsNullWhenNotProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time).build();

        assertNull(service.getComment());
    }

    @Test
    void commentIsSetWhenProvided() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time)
                .comment("Good")
                .build();

        assertEquals("Good", service.getComment());
    }

    @Test
    void commentBlankStringIsConvertedToNull() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time)
                .comment("   ")
                .build();

        assertNull(service.getComment());
    }

    @Test
    void timeNullProducesValidationError() {
        ProvidedService service = new ProvidedService.Builder(null).build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("time")));
    }

    @Test
    void ratingWithinRangeIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time)
                .rating(3)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);

        assertTrue(violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }

    @Test
    void ratingAboveMaxProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(time)
                .rating(6)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating")));
    }
}

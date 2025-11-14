package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HairServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validHairServiceShouldHaveNoViolations() {
        HairService service = new HairService(
                1,
                "Haircut",
                25.0,
                "Basic cut and style",
                30.0,
                HairServiceType.CUT,
                Arrays.asList("Straight", "Curly")
        );

        Set<ConstraintViolation<HairService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid HairService, but got: " + violations);
    }
    @Test
    void nullTypeShouldFailValidation() {
        HairService service = new HairService(
                1,
                "Haircut",
                25.0,
                "Basic cut and style",
                30.0,
                null,
                Arrays.asList("Straight", "Curly")
        );

        Set<ConstraintViolation<HairService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "type"),
                "Expected violation for null 'type', but got: " + violations);
    }
    @Test
    void nullHairTypesShouldFailValidation() {

        assertThrows(IllegalArgumentException.class,
                () -> new HairService(
                        1,
                        "Haircut",
                        25.0,
                        "Basic cut and style",
                        30.0,
                        HairServiceType.CUT,
                        null
                ),
                "Expected IllegalArgumentException when hairTypes is null" );
    }

    @Test
    void emptyHairTypesShouldFailValidation() {
        HairService service = new HairService(
                1,
                "Haircut",
                25.0,
                "Basic cut and style",
                30.0,
                HairServiceType.CUT,
                Collections.emptyList()
        );

        Set<ConstraintViolation<HairService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "hairTypes"),
                "Expected violation for empty 'hairTypes', but got: " + violations);
    }
    @Test
    void hairTypesContainingNullElementShouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HairService(
                        1,
                        "Haircut",
                        25.0,
                        "Basic cut and style",
                        30.0,
                        HairServiceType.CUT,
                        Arrays.asList("Straight", null, "Curly")
                ),
                "Expected IllegalArgumentException when hairTypes contains null element"
        );
    }
    @Test
    void hairTypesContainingBlankElementShouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HairService(
                        1,
                        "Haircut",
                        25.0,
                        "Basic cut and style",
                        30.0,
                        HairServiceType.CUT,
                        Arrays.asList("Straight", "  ", "Curly")
                ),
                "Expected IllegalArgumentException when hairTypes contains blank string"
        );
    }
    @Test
    void getHairTypesShouldReturnCopy() {
        List<String> original = Arrays.asList("Straight", "Curly");
        HairService service = new HairService(
                1,
                "Haircut",
                25.0,
                "Basic cut and style",
                30.0,
                HairServiceType.CUT,
                Arrays.asList("Straight", "Curly")
        );

        List<String> returned = service.getHairTypes();
        assertNotSame(original, returned, "getHairTypes() should return a new copy, not the same list");
        assertEquals(original, returned, "Returned hairTypes list should be equal to the original list");
    }
    private boolean containsViolationFor(Set<ConstraintViolation<HairService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }

}

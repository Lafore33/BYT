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
        List<HairService> serviceList = HairService.getHairServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
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
        List<HairService> serviceList = HairService.getHairServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }
    @Test
    void nullHairTypesShouldFailValidation() {
        List<HairService> serviceList = HairService.getHairServiceList();
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
        List<HairService> serviceListAfter = HairService.getHairServiceList();
        assertEquals(serviceList.size(), serviceListAfter.size(), "The invalid service should not be added to the list");
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
        List<HairService> serviceList = HairService.getHairServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }
    @Test
    void hairTypesContainingNullElementShouldThrowException() {
        List<HairService> serviceList = HairService.getHairServiceList();
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
        List<HairService> serviceListAfter = HairService.getHairServiceList();
        assertEquals(serviceList.size(), serviceListAfter.size(), "The invalid service should not be added to the list");
    }
    @Test
    void hairTypesContainingBlankElementShouldThrowException() {
        List<HairService> serviceList = HairService.getHairServiceList();
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
        List<HairService> serviceListAfter = HairService.getHairServiceList();
        assertEquals(serviceList.size(), serviceListAfter.size(), "The invalid service should not be added to the list");
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

    @Test
    void getServiceListShouldReturnCopy() {
        HairService service = new HairService(
                1,
                "Haircut",
                25.0,
                "Basic cut and style",
                30.0,
                HairServiceType.CUT,
                Arrays.asList("Straight", "Curly")
        );

        List<HairService> listCopy = HairService.getHairServiceList();
        listCopy.clear();

        List<HairService> originalList = HairService.getHairServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<HairService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }

}

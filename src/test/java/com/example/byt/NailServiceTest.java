package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NailServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void validHandNailServiceShouldHaveNoViolations() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                45.0,
                NailServiceType.HAND,
                true
        );

        Set<ConstraintViolation<NailService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid NailService, but got: " + violations);
        List<NailService> serviceList = NailService.getNailServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void nullTypeShouldFailValidation() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                -45.0,
                null,
                true
        );

        Set<ConstraintViolation<NailService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "type"),
                "Expected violation for null 'type', but got: " + violations);
        List<NailService> serviceList = NailService.getNailServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                45.0,
                NailServiceType.HAND,
                true
        );

        List<NailService> listCopy = NailService.getNailServiceList();
        listCopy.clear();

        List<NailService> originalList = NailService.getNailServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<NailService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}

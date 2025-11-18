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
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void blankPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "   ");
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for blank 'purpose', but got: " + violations);
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void nullPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, null);
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for null 'purpose', but got: " + violations);
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "Hydration");

        List<SkinService> listCopy = SkinService.getSkinServiceList();
        listCopy.clear();

        List<SkinService> originalList = SkinService.getSkinServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<SkinService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }


}

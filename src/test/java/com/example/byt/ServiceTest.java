package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validServiceShouldHaveNoViolations() {
        Service service = new Service(1, "Haircut", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(), "Expected no validation errors for valid Service, but got: " + violations);
    }

    @Test
    void negativeIdShouldFailValidation() {
        Service service = new Service(-1, "Haircut", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "id"),
                "Expected violation for field 'id' when negative, but got: " + violations);
    }

    @Test
    void blankNameShouldFailValidation() {
        Service service = new Service(1, "   ", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for blank 'name', but got: " + violations);
    }

    @Test
    void nullNameShouldFailValidation() {
        Service service = new Service(1, null, 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for null 'name', but got: " + violations);
    }

    @Test
    void negativeRegularPriceShouldFailValidation() {
        Service service = new Service(1, "Haircut", -10.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "regularPrice"),
                "Expected violation for negative 'regularPrice', but got: " + violations);
    }

    @Test
    void blankDescriptionShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, "   ", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for blank 'description', but got: " + violations);
    }

    @Test
    void nullDescriptionShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, null, 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for null 'description', but got: " + violations);
    }

    @Test
    void negativeDurationShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, "Basic haircut", -5.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "duration"),
                "Expected violation for negative 'duration', but got: " + violations);
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Service>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }



}

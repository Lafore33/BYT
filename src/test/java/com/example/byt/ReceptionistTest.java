package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionistTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validReceptionistPassesValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Receptionist receptionist = new Receptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                WorkType.FULL_TIME
        );
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        assertTrue(violations.isEmpty(), "Valid receptionist should have no validation violations");
    }

    @Test
    void nullWorkTypeFailsValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);

        Receptionist receptionist = new Receptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                null
        );
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        assertTrue(containsViolationFor(violations, "workType"),
                "Expected violation for null 'workType', but got: " + violations);
    }
    private boolean containsViolationFor(Set<ConstraintViolation<Receptionist>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
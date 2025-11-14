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

    private static class TestReceptionist extends Receptionist {
        public TestReceptionist(String name,
                                String surname,
                                String phoneNumber,
                                LocalDate birthDate,
                                WorkType workType) {
            super(name, surname, phoneNumber, birthDate, workType);
        }
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validReceptionistPassesValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);

        TestReceptionist receptionist = new TestReceptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                WorkType.FULL_TIME
        );
        Set<ConstraintViolation<TestReceptionist>> violations = validator.validate(receptionist);
        assertTrue(violations.isEmpty(), "Valid receptionist should have no validation violations");
    }

    @Test
    void nullWorkTypeFailsValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);

        TestReceptionist receptionist = new TestReceptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                null
        );
        Set<ConstraintViolation<TestReceptionist>> violations = validator.validate(receptionist);
        assertTrue(containsViolationFor(violations, "workType"),
                "Expected violation for null 'workType', but got: " + violations);
    }
    private boolean containsViolationFor(Set<ConstraintViolation<TestReceptionist>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
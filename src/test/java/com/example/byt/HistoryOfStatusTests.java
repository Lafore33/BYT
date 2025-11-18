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

public class HistoryOfStatusTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validHistoryOfStatusShouldHaveNoViolations() {
        LocalDate date = LocalDate.now().minusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, date);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid HistoryOfStatus, but got: " + violations);
        assertTrue(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Valid HistoryOfStatus should be added to extent");
    }

    @Test
    void nullStatusShouldFailValidation() {
        LocalDate date = LocalDate.now().minusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(null, date);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertFalse(violations.isEmpty(),
                "Expected validation violations for null 'status', but got none");
        assertTrue(containsViolationFor(violations, "status"),
                "Expected violation for field 'status', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    @Test
    void nullDateShouldFailValidation() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, null);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertFalse(violations.isEmpty(),
                "Expected validation violations for null 'dateOfChangingStatus', but got none");
        assertTrue(containsViolationFor(violations, "dateOfChangingStatus"),
                "Expected violation for field 'dateOfChangingStatus', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    @Test
    void futureDateShouldFailValidation() {
        LocalDate future = LocalDate.now().plusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, future);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertFalse(violations.isEmpty(),
                "Expected validation violations for future 'dateOfChangingStatus', but got none");
        assertTrue(containsViolationFor(violations, "dateOfChangingStatus"),
                "Expected violation for field 'dateOfChangingStatus', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<HistoryOfStatus>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
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
    void validHistoryOfStatusHasNoViolations() {
        LocalDate date = LocalDate.now();
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, date);

        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullStatusProducesViolation() {
        LocalDate date = LocalDate.now();
        HistoryOfStatus history = new HistoryOfStatus(null, date);

        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    void nullDateProducesViolation() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, null);

        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateOfChangingStatus")));
    }

    @Test
    void futureDateProducesViolation() {
        LocalDate future = LocalDate.now().plusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, future);

        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateOfChangingStatus")));
    }
}

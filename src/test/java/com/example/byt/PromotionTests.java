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

public class PromotionTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPromotionHasNoViolations() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Black Friday", "Big discount",
                30.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid promotion");
    }

    @Test
    void invalidPercentageProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Too small", "Invalid",
                3.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "percentage"),
                "Expected violation for 'percentage' field");
    }

    @Test
    void setEndDateWithValidValueUpdatesEndDate() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate newEnd = LocalDate.of(2025, 12, 15);
        promotion.setEndDate(newEnd);

        assertEquals(newEnd, promotion.getEndDate());
    }

    @Test
    void setEndDateWithEarlierDateThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate invalidEnd = LocalDate.of(2025, 10, 15);

        assertThrows(RuntimeException.class, () -> promotion.setEndDate(invalidEnd));
    }

    @Test
    void setEndDateToNullThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        assertThrows(RuntimeException.class, () -> promotion.setEndDate(null));
    }

    @Test
    void setStartDateWithValidValueUpdatesStartDate() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate newStart = LocalDate.of(2025, 10, 15);
        promotion.setStartDate(newStart);

        assertEquals(newStart, promotion.getStartDate());
    }

    @Test
    void setStartDateWithLaterDateThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate invalidStart = LocalDate.of(2025, 12, 15);

        assertThrows(RuntimeException.class, () -> promotion.setStartDate(invalidStart));
    }

    @Test
    void setStartDateToNullThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        assertThrows(RuntimeException.class, () -> promotion.setStartDate(null));
    }

    @Test
    void blankNameProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("   ", "Valid description",
                20.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for 'name' field");
    }

    @Test
    void nullNameProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion(null, "Valid description",
                20.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for 'name' field");
    }

    @Test
    void blankDescriptionProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Promo", "   ",
                20.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for 'description' field");
    }

    @Test
    void nullDescriptionProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Promo", null,
                20.0, start, end);

        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for 'description' field");
    }

    @Test
    void nullStartDateThrowsException() {
        LocalDate end = LocalDate.of(2025, 11, 30);

        assertThrows(IllegalArgumentException.class, () ->
                new Promotion("Promo", "Desc", 20.0, null, end)
        );
    }

    @Test
    void nullEndDateThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);

        assertThrows(IllegalArgumentException.class, () ->
                new Promotion("Promo", "Desc", 20.0, start, null)
        );
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Promotion>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
package com.example.byt;

import com.example.byt.models.Promotion;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsCorrectValues() {
        String name = "Black Friday";
        String description = "Big discount";
        double percentage = 30.0;
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion(name, description,
                percentage, start, end);
        assertEquals(name, promotion.getName(), "Incorrect name set in the constructor");
        assertEquals(description, promotion.getDescription(), "Incorrect description set in the constructor");
        assertEquals(percentage, promotion.getPercentage(), "Incorrect percentage set in the constructor");
        assertEquals(start, promotion.getStartDate(), "Incorrect start date set in the constructor");
        assertEquals(end, promotion.getEndDate(), "Incorrect end date set in the constructor");
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
        assertTrue(Promotion.getPromotionList().contains(promotion),
                "Valid promotion should be added to extent");
    }

    @Test
    void smallerPercentageProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Too small", "Invalid",
                3.0, start, end);
        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "percentage"),
                "Expected violation for 'percentage' field");
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
    }

    @Test
    void biggerPercentageProducesViolation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Too big", "Invalid",
                60.0, start, end);
        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        assertTrue(containsViolationFor(violations, "percentage"),
                "Expected violation for 'percentage' field");
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
    }
    @Test
    void endDateWithAfterStartDateNotThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        assertDoesNotThrow(() -> new Promotion("Sale", "Discount", 20.0, start, end), "Expected no exceptions creating promotion with valid dates");
    }

    @Test
    void endDateBeforeStartDateThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate invalidEnd = LocalDate.of(2025, 10, 15);
        assertThrows(IllegalArgumentException.class, () -> new Promotion("Sale", "Discount", 20.0, start, invalidEnd));
    }

    @Test
    void endDateToNullThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        assertThrows(IllegalArgumentException.class, () -> new Promotion("Sale", "Discount", 20.0, start, null));
    }

    @Test
    void startDateToNullThrowsException() {
        LocalDate end = LocalDate.of(2025, 11, 30);
        assertThrows(RuntimeException.class, () -> new Promotion("Sale", "Discount", 20.0, null, end));
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
    void setEndDateBeforeStartDateThrowsException() {
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
    void setStartDateAfterEndDateThrowsException() {
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
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
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
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
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
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
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
        assertFalse(Promotion.getPromotionList().contains(promotion),
                "Invalid promotion should NOT be added to extent");
    }

    @Test
    void getPromotionListReturnsCopy(){
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Black Friday", "Big discount",
                30.0, start, end);
        List<Promotion> listCopy = Promotion.getPromotionList();
        listCopy.clear();
        List<Promotion> original = Promotion.getPromotionList();
        assertTrue(original.contains(promotion), "List should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Promotion>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
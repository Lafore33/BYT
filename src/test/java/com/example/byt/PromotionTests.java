package com.example.byt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionTests {

    @Test
    void promotionIsCreatedCorrectly() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Black Friday", "Big discount",
                30.0, start, end);

        assertNotNull(promotion);
        assertEquals("Black Friday", promotion.getName());
        assertEquals("Big discount", promotion.getDescription());
        assertEquals(30.0, promotion.getPercentage());
        assertEquals(start, promotion.getStartDate());
        assertEquals(end, promotion.getEndDate());
    }

    @Test
    void promotionPercentageAtBoundsIsAllowed() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion p1 = new Promotion("Sale 5", "Min", 5.0, start, end);
        assertEquals(5.0, p1.getPercentage());

        Promotion p2 = new Promotion("Sale 50", "Max", 50.0, start, end);
        assertEquals(50.0, p2.getPercentage());
    }

    @Test
    void promotionWithEndDateBeforeStartDateThrowsException() {
        LocalDate start = LocalDate.of(2025, 11, 30);
        LocalDate end = LocalDate.of(2025, 11, 1);

        assertThrows(IllegalArgumentException.class, () ->
                new Promotion("Invalid", "Wrong dates", 20.0, start, end)
        );
    }

    @Test
    void setEndDateWithValidAndInvalidValues() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate newEnd = LocalDate.of(2025, 12, 15);
        promotion.setEndDate(newEnd);
        assertEquals(newEnd, promotion.getEndDate());

        LocalDate invalidEnd = LocalDate.of(2025, 10, 15);
        assertThrows(IllegalArgumentException.class, () ->
                promotion.setEndDate(invalidEnd)
        );
    }

    @Test
    void setStartDateWithValidAndInvalidValues() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);

        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);

        LocalDate newStart = LocalDate.of(2025, 10, 15);
        promotion.setStartDate(newStart);
        assertEquals(newStart, promotion.getStartDate());

        LocalDate invalidStart = LocalDate.of(2025, 12, 15);
        assertThrows(IllegalArgumentException.class, () ->
                promotion.setStartDate(invalidStart)
        );
    }

    @Test
    void promotionWithSameStartAndEndDateIsAllowed() {
        LocalDate date = LocalDate.of(2025, 11, 11);

        Promotion promotion = new Promotion("One Day", "Flash", 15.0, date, date);

        assertEquals(date, promotion.getStartDate());
        assertEquals(date, promotion.getEndDate());
    }
}

package com.example.byt;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerPromotionTests {

    @Test
    void testCustomerWithEmail() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                "john@example.com", birthDate);
        assertNotNull(customer);
        assertEquals("John", customer.getName());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void testCustomerWithoutEmail() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("Jane", "Smith", "987654321", birthDate);
        assertNotNull(customer);
        assertEquals("Jane", customer.getName());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void testCustomerDefaultStatus() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("Test", "User", "111", birthDate);
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void testCustomerGetAge() {
        LocalDate birthDate = LocalDate.now().minusYears(25).minusDays(1);
        Customer customer = new Customer("Test", "User", "111", birthDate);
        assertEquals(25, customer.getAge());
    }

    @Test
    void testCustomerGetAgeYoung() {
        LocalDate birthDate = LocalDate.now().minusYears(18).minusDays(1);
        Customer customer = new Customer("Young", "User", "222", birthDate);
        assertEquals(18, customer.getAge());
    }

    @Test
    void testCustomerGetAgeElder() {
        LocalDate birthDate = LocalDate.now().minusYears(70).minusDays(1);
        Customer customer = new Customer("Elder", "User", "333", birthDate);
        assertEquals(70, customer.getAge());
    }

    @Test
    void testPromotionCreation() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Black Friday", "Big discount",
                30.0, start, end);
        assertNotNull(promotion);
        assertEquals("Black Friday", promotion.getName());
        assertEquals(30.0, promotion.getPercentage());
    }

    @Test
    void testPromotionPercentageRange() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion p1 = new Promotion("Sale 5", "Min", 5.0, start, end);
        assertEquals(5.0, p1.getPercentage());
        Promotion p2 = new Promotion("Sale 50", "Max", 50.0, start, end);
        assertEquals(50.0, p2.getPercentage());
    }

    @Test
    void testPromotionInvalidDates() {
        LocalDate start = LocalDate.of(2025, 11, 30);
        LocalDate end = LocalDate.of(2025, 11, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            new Promotion("Invalid", "Wrong dates", 20.0, start, end);
        });
    }

    @Test
    void testPromotionSetEndDate() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);
        LocalDate newEnd = LocalDate.of(2025, 12, 15);
        promotion.setEndDate(newEnd);
        assertEquals(newEnd, promotion.getEndDate());
        LocalDate invalidEnd = LocalDate.of(2025, 10, 15);
        assertThrows(IllegalArgumentException.class, () -> {
            promotion.setEndDate(invalidEnd);
        });
    }

    @Test
    void testPromotionSetStartDate() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Sale", "Discount", 20.0, start, end);
        LocalDate newStart = LocalDate.of(2025, 10, 15);
        promotion.setStartDate(newStart);
        assertEquals(newStart, promotion.getStartDate());
        LocalDate invalidStart = LocalDate.of(2025, 12, 15);
        assertThrows(IllegalArgumentException.class, () -> {
            promotion.setStartDate(invalidStart);
        });
    }

    @Test
    void testPromotionSameDate() {
        LocalDate date = LocalDate.of(2025, 11, 11);
        Promotion promotion = new Promotion("One Day", "Flash", 15.0, date, date);
        assertEquals(date, promotion.getStartDate());
        assertEquals(date, promotion.getEndDate());
    }

    @Test
    void testCustomerEmailAttribute() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                "john@example.com", birthDate);
        assertEquals("john@example.com", customer.getEmailAddress());
    }

    @Test
    void testPromotionDescriptionAttribute() {
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end = LocalDate.of(2025, 11, 30);
        Promotion promotion = new Promotion("Black Friday", "Big discount",
                30.0, start, end);
        assertEquals("Big discount", promotion.getDescription());
    }
}

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

public class CustomerTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCustomerEmailHasNoViolations() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                "john@example.com", birthDate);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidCustomerEmailProducesViolation() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                "not-an-email", birthDate);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("emailAddress")));
    }

    @Test
    void customerWithEmailIsCreatedCorrectly() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                "john@example.com", birthDate);

        assertNotNull(customer);
        assertEquals("John", customer.getName());
        assertEquals("john@example.com", customer.getEmailAddress());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }


    @Test
    void customerWithoutEmailIsCreatedCorrectly() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("Jane", "Smith", "987654321", birthDate);
        assertNotNull(customer);
        assertEquals("Jane", customer.getName());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void customerDefaultStatusIsGood() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("Test", "User", "111", birthDate);
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void ageIsCalculatedCorrectly() {
        LocalDate birthDate = LocalDate.now().minusYears(25).minusDays(1);
        Customer customer = new Customer("Age", "Test", "555", birthDate);

        assertEquals(25, customer.getAge());
    }
}

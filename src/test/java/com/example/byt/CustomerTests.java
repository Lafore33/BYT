package com.example.byt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTests {

    @Test
    void customerWithEmailIsCreatedCorrectly() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        Customer customer = new Customer("John", "Doe", "123456789",
                "john@example.com", birthDate);

        assertNotNull(customer);
        assertEquals("John", customer.getName());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
        assertEquals("john@example.com", customer.getEmailAddress());
    }

    @Test
    void customerWithoutEmailIsCreatedCorrectly() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        Customer customer = new Customer("Jane", "Smith", "987654321", birthDate);

        assertNotNull(customer);
        assertEquals("Jane", customer.getName());
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
        assertNull(customer.getEmailAddress());
    }

    @Test
    void customerDefaultStatusIsGood() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        Customer customer = new Customer("Test", "User", "111", birthDate);

        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void getAgeForAdultCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(25).minusDays(1);

        Customer customer = new Customer("Test", "User", "111", birthDate);

        assertEquals(25, customer.getAge());
    }

    @Test
    void getAgeForYoungCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(18).minusDays(1);

        Customer customer = new Customer("Young", "User", "222", birthDate);

        assertEquals(18, customer.getAge());
    }

    @Test
    void getAgeForElderCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(70).minusDays(1);

        Customer customer = new Customer("Elder", "User", "333", birthDate);

        assertEquals(70, customer.getAge());
    }
}

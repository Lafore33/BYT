package com.example.byt;

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

public class CustomerTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsCorrectValuesForCustomerWithEmail(){
        String name = "John";
        String surname = "Doe";
        String phoneNumber = "+123456789";
        String emailAddress = "john@example.com";
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer(name, surname, phoneNumber, emailAddress, dateOfBirth);
        assertEquals(name, customer.getName(), "Incorrect customer name set in the constructor");
        assertEquals(surname, customer.getSurname(), "Incorrect customer surname set in the constructor");
        assertEquals(phoneNumber, customer.getPhoneNumber(), "Incorrect customer phone number set in the constructor");
        assertEquals(emailAddress, customer.getEmailAddress(), "Incorrect customer email address set in the constructor");
        assertEquals(dateOfBirth, customer.getBirthDate(), "Incorrect customer birth date set in the constructor");
    }
    @Test
    void constructorSetsCorrectValuesForCustomerWithoutEmail() {
        String name = "John";
        String surname = "Doe";
        String phoneNumber = "+123456789";
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer(name, surname, phoneNumber, dateOfBirth);
        assertEquals(name, customer.getName(), "Incorrect customer name set in the constructor");
        assertEquals(surname, customer.getSurname(), "Incorrect customer surname set in the constructor");
        assertEquals(phoneNumber, customer.getPhoneNumber(), "Incorrect customer phone number set in the constructor");
        assertEquals(dateOfBirth, customer.getBirthDate(), "Incorrect customer birth date set in the constructor");
    }

    @Test
    void customerDefaultStatusIsGood() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("Test", "User", "111", birthDate);
        assertEquals(CustomerStatus.GOOD, customer.getCustomerStatus());
    }

    @Test
    void validCustomerHasNoViolations() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "+123456789",
                "john@example.com", birthDate);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid customer with email");
        assertTrue(Customer.getCustomerList().contains(customer),
                "Valid customer should be added to extent");
    }

    @Test
    void invalidCustomerEmailProducesViolation() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "+123456789",
                "not-an-email", birthDate);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(containsViolationFor(violations, "emailAddress"),
                "Expected violation for 'emailAddress' field");
        assertFalse(Customer.getCustomerList().contains(customer),
                "Invalid customer should NOT be added to extent");
    }

    @Test
    void blankCustomerEmailProducesViolation() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Customer customer = new Customer("John", "Doe", "123456789",
                " ", birthDate);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(containsViolationFor(violations, "emailAddress"),
                "Expected violation for 'emailAddress' field");
        assertFalse(Customer.getCustomerList().contains(customer),
                "Invalid customer should NOT be added to extent");
    }

    @Test
    void ageIsCalculatedCorrectly() {
        LocalDate birthDate = LocalDate.now().minusYears(25).minusDays(1);
        Customer customer = new Customer("Age", "Test", "555", birthDate);
        assertEquals(25, customer.getAge());
    }

    @Test
    void getCustomerListShouldReturnCopy() {
        Customer customer = new Customer("Test", "User", "111", LocalDate.now().minusYears(25).minusDays(1));

        List<Customer> listCopy = Customer.getCustomerList();
        listCopy.clear();

        List<Customer> originalList = Customer.getCustomerList();
        assertTrue(originalList.contains(customer), "The original list should not be modified");
    }

    @Test
    void setEmailAddressSetsValuesCorrectly() {
        Customer customer = new Customer("Test", "User", "111", LocalDate.now().minusYears(25).minusDays(1));
        String email = "exmple@example.com";
        customer.setEmailAddress(email);
        assertEquals(email, customer.getEmailAddress());
    }

    @Test
    void settingCorrectEmailPassValidation(){
        Customer customer = new Customer("Test", "User", "111", LocalDate.now().minusYears(25).minusDays(1));
        String email = "exmple@example.com";
        customer.setEmailAddress(email);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty(), "Expected no violations for valid customer with email");
    }

    @Test
    void settingInvalidEmailFailsValidation(){
        Customer customer = new Customer("Test", "User", "111", LocalDate.now().minusYears(25).minusDays(1));
        String email = "not-email";
        customer.setEmailAddress(email);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(containsViolationFor(violations, "emailAddress"), "Expected violations for invalid email address");
    }

    @Test
    void settingBlankEmailFailsValidation(){
        Customer customer = new Customer("Test", "User", "111", LocalDate.now().minusYears(25).minusDays(1));
        String email = " ";
        customer.setEmailAddress(email);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(containsViolationFor(violations, "emailAddress"), "Expected violations for blank email address");
    }



    private boolean containsViolationFor(Set<ConstraintViolation<Customer>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
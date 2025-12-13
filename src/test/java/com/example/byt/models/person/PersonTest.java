package com.example.byt.models.person;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private static Validator validator;

    private Person createValidPersonViaCustomer(String name, String surname, String phoneNumber, LocalDate birthDate) {
        return Person.createCustomer(
                name,
                surname,
                phoneNumber,
                birthDate
        ).getPerson();
    }

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        String name = "Yelizaveta";
        String surname = "Gaiduk";
        String phoneNumber = "+48123456789";
        LocalDate birthDate = LocalDate.now().minusYears(25);

        Person person = createValidPersonViaCustomer(name, surname, phoneNumber, birthDate);
        assertEquals(name, person.getName(), "Incorrect name set in the constructor");
        assertEquals(surname, person.getSurname(), "Incorrect surname set in the constructor");
        assertEquals(phoneNumber, person.getPhoneNumber(), "Incorrect phoneNumber set in the constructor");
        assertEquals(birthDate, person.getBirthDate(), "Incorrect birthDate set in the constructor");
    }

    @Test
    void validPersonPassesValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(violations.isEmpty(),"Valid person should have no validation violations");
    }

    @Test
    void blankNameFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "  ",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for blank 'name', but got: " + violations);
    }

    @Test
    void nullNameFailsValidation() {
        Person person = createValidPersonViaCustomer(
                null,
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for null 'name', but got: " + violations);
    }

    @Test
    void blankSurnameFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                "   ",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "surname"),"Expected violation for blank 'surname', but got: " + violations);
    }

    @Test
    void nullSurnameFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                null,
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<Person>> violations = validator.validate(person);assertTrue(containsViolationFor(violations, "surname"),
                "Expected violation for null 'surname', but got: " + violations);
    }

    @Test
    void blankPhoneNumberFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                "Gaiduk",
                "   ",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for blank 'phoneNumber', but got: " + violations);
    }

    @Test
    void nullPhoneNumberFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                "Gaiduk",
                null,
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for null 'phoneNumber', but got: " + violations);
    }

    @Test
    void nonDigitPhoneNumberFailsValidation() {
        Person person = createValidPersonViaCustomer(
                "Yelizaveta",
                "Gaiduk",
                "abc123",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for non-digit 'phoneNumber', but got: " + violations);
    }

    @Test
    void nullBirthDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                        createValidPersonViaCustomer(
                                "Yelizaveta",
                                "Gaiduk",
                                "+48123456789",
                                null
                        ),
                "Expected IllegalArgumentException for null 'birthDate'"
        );
    }

    @Test
    void underageBirthDateThrowsException() {
        LocalDate tooYoung = LocalDate.now().minusYears(17);
        assertThrows(IllegalArgumentException.class, () ->
                createValidPersonViaCustomer("Yelizaveta", "Gaiduk", "+48123456789", tooYoung
                ),"Expected IllegalArgumentException for underage birthDate"
        );
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Person>> violations, String field) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
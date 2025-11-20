package com.example.byt.person;

import com.example.byt.models.person.Person;
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

    private static class TestPerson extends Person {
        public TestPerson(String name, String surname, String phoneNumber, LocalDate birthDate) {
            super(name, surname, phoneNumber, birthDate);
        }
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

        TestPerson person = new TestPerson(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        assertEquals(name, person.getName(), "Incorrect name set in the constructor");
        assertEquals(surname, person.getSurname(), "Incorrect surname set in the constructor");
        assertEquals(phoneNumber, person.getPhoneNumber(), "Incorrect phoneNumber set in the constructor");
        assertEquals(birthDate, person.getBirthDate(), "Incorrect birthDate set in the constructor");
    }

    @Test
    void validPersonPassesValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(violations.isEmpty(),"Valid person should have no validation violations");
    }

    @Test
    void blankNameFailsValidation() {
        TestPerson person = new TestPerson(
                "  ",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for blank 'name', but got: " + violations);
    }

    @Test
    void nullNameFailsValidation() {
        TestPerson person = new TestPerson(
                null,
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for null 'name', but got: " + violations);
    }

    @Test
    void blankSurnameFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "   ",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "surname"),"Expected violation for blank 'surname', but got: " + violations);
    }

    @Test
    void nullSurnameFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                null,
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);assertTrue(containsViolationFor(violations, "surname"),
                "Expected violation for null 'surname', but got: " + violations);
    }

    @Test
    void blankPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "   ",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for blank 'phoneNumber', but got: " + violations);
    }

    @Test
    void nullPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                null,
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for null 'phoneNumber', but got: " + violations);
    }

    @Test
    void nonDigitPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "abc123",
                LocalDate.now().minusYears(25)
        );
        Set<ConstraintViolation<TestPerson>> violations = validator.validate(person);
        assertTrue(containsViolationFor(violations, "phoneNumber"),"Expected violation for non-digit 'phoneNumber', but got: " + violations);
    }

    @Test
    void nullBirthDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                        new TestPerson(
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
                new TestPerson("Yelizaveta", "Gaiduk", "+48123456789", tooYoung
                ),"Expected IllegalArgumentException for underage birthDate"
        );
    }

    private boolean containsViolationFor(Set<ConstraintViolation<TestPerson>> violations, String field) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
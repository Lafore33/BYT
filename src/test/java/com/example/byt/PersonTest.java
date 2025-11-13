package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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
        assertTrue(violations.isEmpty(),
                "Valid person should have no validation violations");
    }

    @Test
    void blankNameFailsValidation() {
        TestPerson person = new TestPerson(
                "  ",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "name"));
    }

    @Test
    void nullNameFailsValidation() {
        TestPerson person = new TestPerson(
                null,
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "name"));
    }

    @Test
    void blankSurnameFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "   ",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "surname"));
    }

    @Test
    void nullSurnameFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                null,
                "+48123456789",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "surname"));
    }

    @Test
    void blankPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "   ",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "phoneNumber"));
    }

    @Test
    void nullPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                null,
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "phoneNumber"));
    }

    @Test
    void nonDigitPhoneNumberFailsValidation() {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "abc123",
                LocalDate.now().minusYears(25)
        );
        assertTrue(containsViolationFor(validator.validate(person), "phoneNumber"));
    }

    @Test
    void nullBirthDateFailsValidation() throws Exception {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Field birthDate = Person.class.getDeclaredField("birthDate");
        birthDate.setAccessible(true);
        birthDate.set(person, null);
        assertTrue(containsViolationFor(validator.validate(person), "birthDate"));
    }

    @Test
    void birthDateTodayFailsPastValidation() throws Exception {
        TestPerson person = new TestPerson(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25)
        );

        Field birthDate = Person.class.getDeclaredField("birthDate");
        birthDate.setAccessible(true);
        birthDate.set(person, LocalDate.now());
        assertTrue(containsViolationFor(validator.validate(person), "birthDate"));
    }

    @Test
    void constructorThrowsForUnderageBirthDate() {
        LocalDate tooYoung = LocalDate.now().minusYears(17);
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Yelizaveta",
                        "Gaiduk",
                        "+48123456789",
                        tooYoung
                )
        );
    }

    private boolean containsViolationFor(Set<? extends ConstraintViolation<?>> violations, String field) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(field));
    }
}
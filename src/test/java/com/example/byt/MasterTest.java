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

class MasterTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        String name = "Yelizaveta";
        String surname = "Gaiduk";
        String phoneNumber = "+48123456789";
        LocalDate dateOfBirth = LocalDate.now().minusYears(25);
        int experience = 5;
        Master master = new Master(
                name,
                surname,
                phoneNumber,
                dateOfBirth,
                experience
        );
        assertEquals(name, master.getName(), "Incorrect name set in the constructor");
        assertEquals(surname, master.getSurname(), "Incorrect surname set in the constructor");
        assertEquals(phoneNumber, master.getPhoneNumber(), "Incorrect phone number set in the constructor");
        assertEquals(dateOfBirth, master.getBirthDate(), "Incorrect date set in the constructor");
        assertEquals(experience, master.getExperience(), "Incorrect experience set in the constructor");
    }

    @Test
    void validMasterShouldHaveNoViolations() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                5
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(),
                "Valid master should have no validation violations, but got: " + violations);
    }

    @Test
    void zeroExperienceShouldBeValid() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                0
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(), "Valid master should have no validation violations, but got: " + violations);
    }

    @Test
    void negativeExperienceShouldFailValidation() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                -1
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(containsViolationFor(violations, "experience"),
                "Expected violation for negative 'experience', but got: " + violations);
    }
    @Test
    void getMinExperienceForTopREturnsCorrectValue() {
        assertEquals(3, Master.getMinExperienceForTop(), "Incorrect min experience for top, should be 3");
    }
    private boolean containsViolationFor(Set<ConstraintViolation<Master>> violations,
                                         String fieldName) {
        return violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals(fieldName));
    }
}

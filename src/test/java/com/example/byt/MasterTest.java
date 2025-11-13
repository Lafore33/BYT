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

    private static class TestMaster extends Master {
        public TestMaster(String name,
                          String surname,
                          String phoneNumber,
                          LocalDate birthDate,
                          int experience) {
            super(name, surname, phoneNumber, birthDate, experience);
        }
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validMasterShouldHaveNoViolations() {
        TestMaster master = new TestMaster(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                5
        );
        Set<ConstraintViolation<TestMaster>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(),
                "Valid master should have no validation violations");
    }

    @Test
    void zeroExperienceShouldBeValid() {
        TestMaster master = new TestMaster(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                0
        );
        Set<ConstraintViolation<TestMaster>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(), "experience = 0 should satisfy @Min(0)");
    }

    @Test
    void negativeExperienceShouldFailValidation() {
        TestMaster master = new TestMaster(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                -1
        );
        Set<ConstraintViolation<TestMaster>> violations = validator.validate(master);
        assertFalse(violations.isEmpty(), "Negative experience must fail validation");
        assertTrue(containsViolationFor(violations, "experience"),
                "Expected violation for 'experience', but got: " + violations);
    }

    @Test
    void constructorSetsExperienceCorrectly() throws Exception {
        TestMaster master = new TestMaster(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                7
        );

        Field experienceField = Master.class.getDeclaredField("experience");
        experienceField.setAccessible(true);
        assertEquals(7, experienceField.get(master),
                "Constructor should assign correct experience value");
    }
    private boolean containsViolationFor(Set<ConstraintViolation<TestMaster>> violations,
                                         String fieldName) {
        return violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals(fieldName));
    }
}

package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoHandsServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTwoHandsServiceShouldHaveNoViolations() {
        TwoHandsService service = new TwoHandsService(
                1,
                "Massage",
                80.0,
                "Full body massage with two hands",
                60.0
        );

        Set<ConstraintViolation<TwoHandsService>> violations = validator.validate(service);

        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid TwoHandsService, but got: " + violations);
    }

    @Test
    void numOfSpecialistsRequiredShouldReturnOne() {
        assertEquals(1, TwoHandsService.getNumOfSpecialistsRequired(),
                "Expected numOfSpecialistsRequired to return 1");
    }
}

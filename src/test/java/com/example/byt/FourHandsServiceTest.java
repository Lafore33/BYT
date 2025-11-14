package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FourHandsServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validFourHandsServiceShouldHaveNoViolations() {
        FourHandsService service = new FourHandsService(
                1,
                "Full Body Massage",
                120.0,
                "Relaxing full body massage",
                60.0,
                true
        );

        Set<ConstraintViolation<FourHandsService>> violations = validator.validate(service);

        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid FourHandsService, but got: " + violations);
    }
    @Test
    void numOfSpecialistsRequiredShouldReturnTwo() {
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired(),
                "Expected numOfSpecialistsRequired to return 2");
    }



}

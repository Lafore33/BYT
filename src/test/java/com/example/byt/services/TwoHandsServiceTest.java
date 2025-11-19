package com.example.byt.services;

import com.example.byt.models.services.TwoHandsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    void constructorSetsValuesCorrectly(){
        int id = 1;
        String name  = "Massage";
        String description = "Full body massage with two hands";
        double regularPrice = 80.0;
        double duration = 60.0;
        TwoHandsService service = new TwoHandsService(
                id,
                name,
                regularPrice,
                description,
                duration
        );
        assertEquals(id, service.getId(), "Incorrect id set in the constructor");
        assertEquals(name, service.getName(), "Incorrect name set in the constructor");
        assertEquals(description, service.getDescription(), "Incorrect description set in the constructor");
        assertEquals(duration, service.getDuration(), "Incorrect duration set in the constructor");
        assertEquals(regularPrice, service.getRegularPrice(), "Incorrect regular price set in the constructor");
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
        List<TwoHandsService> serviceList = TwoHandsService.getTwoHandsServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        TwoHandsService service = new TwoHandsService(
                1,
                "Massage",
                80.0,
                "Full body massage with two hands",
                60.0
        );

        List<TwoHandsService> listCopy = TwoHandsService.getTwoHandsServiceList();
        listCopy.clear();

        List<TwoHandsService> originalList = TwoHandsService.getTwoHandsServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    @Test
    void numOfSpecialistsRequiredShouldReturnOne() {
        assertEquals(1, TwoHandsService.getNumOfSpecialistsRequired(),
                "Expected numOfSpecialistsRequired to return 1");
    }
}

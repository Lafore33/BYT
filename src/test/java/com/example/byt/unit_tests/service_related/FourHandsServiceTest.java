package com.example.byt.unit_tests.service_related;

import com.example.byt.models.service_related.FourHandsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    void constructorSetsValuesCorrectly(){
        int id = 1;
        String name = "Full Body Massage";
        double regularPrice = 120.0;
        String description = "Relaxing full body massage";
        double duration = 60.0;
        boolean isExpressService = true;
        FourHandsService service = new FourHandsService(
                id,
                name,
                regularPrice,
                description,
                duration,
                isExpressService
        );
        assertEquals(id, service.getId(), "Incorrect id set in the constructor");
        assertEquals(name, service.getName(), "Incorrect name set in the constructor");
        assertEquals(regularPrice, service.getRegularPrice(), "Incorrect regular price set in the constructor");
        assertEquals(description, service.getDescription(), "Incorrect description set in the constructor");
        assertEquals(duration, service.getDuration(), "Incorrect duration set in the constructor");
        assertEquals(isExpressService, service.isExpressService(), "Incorrect isExpressService set in the constructor");

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
        List<FourHandsService> serviceList = FourHandsService.getFourHandsServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        FourHandsService service = new FourHandsService(
                1,
                "Full Body Massage",
                120.0,
                "Relaxing full body massage",
                60.0,
                true
        );

        List<FourHandsService> listCopy = FourHandsService.getFourHandsServiceList();
        listCopy.clear();

        List<FourHandsService> originalList = FourHandsService.getFourHandsServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    @Test
    void numOfSpecialistsRequiredShouldReturnTwo() {
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired(),
                "Expected numOfSpecialistsRequired to return 2");
    }
}

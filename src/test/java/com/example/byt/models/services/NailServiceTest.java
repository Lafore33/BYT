package com.example.byt.models.services;

import com.example.byt.models.services.NailService;
import com.example.byt.models.services.NailServiceType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NailServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @BeforeEach
    void clearExtent(){
        NailService.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        int id  = 1;
        String name = "Manicure";
        double regularPrice = 30.0;
        String description = "Basic hand treatment";
        double duration = 45.0;
        NailServiceType type = NailServiceType.HAND;
        boolean isCareIncluded = true;
        NailService service = new NailService(
                id,
                name,
                regularPrice,
                description,
                duration,
                type,
                isCareIncluded
        );
        assertEquals(id, service.getId(), "Incorrect id set in the constructor");
        assertEquals(name, service.getName(), "Incorrect name set in the constructor");
        assertEquals(regularPrice, service.getRegularPrice(), "Incorrect regular price");
        assertEquals(description, service.getDescription(), "Incorrect description set in the constructor");
        assertEquals(duration, service.getDuration(), "Incorrect duration set in the constructor");
        assertEquals(type, service.getType(), "Incorrect type set in the constructor");
        assertEquals(isCareIncluded, service.isCareIncluded(), "Incorrect isCareIncluded set in the constructor");

    }
    @Test
    void validHandNailServiceShouldHaveNoViolations() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                45.0,
                NailServiceType.HAND,
                true
        );

        Set<ConstraintViolation<NailService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid NailService, but got: " + violations);
        List<NailService> serviceList = NailService.getNailServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void nullTypeShouldFailValidation() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                -45.0,
                null,
                true
        );

        Set<ConstraintViolation<NailService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "type"),
                "Expected violation for null 'type', but got: " + violations);
        List<NailService> serviceList = NailService.getNailServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        NailService service = new NailService(
                1,
                "Manicure",
                30.0,
                "Basic hand treatment",
                45.0,
                NailServiceType.HAND,
                true
        );

        List<NailService> listCopy = NailService.getNailServiceList();
        listCopy.clear();

        List<NailService> originalList = NailService.getNailServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<NailService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}

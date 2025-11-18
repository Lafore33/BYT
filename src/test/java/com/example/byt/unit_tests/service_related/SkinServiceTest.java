package com.example.byt.unit_tests.service_related;

import com.example.byt.models.service_related.SkinService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SkinServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void clearExtent() {
        SkinService.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectly(){
        int id  =1;
        String name = "Facial" ;
        String description = "Deep cleansing";
        double regularPrice = 50.0;
        double duration = 60.0;
        String purpose = "Hydration";
        SkinService service = new SkinService(
                id,
                name,
                regularPrice,
                description,
                duration,
                purpose);
        assertEquals(id, service.getId(), "Incorrect id set in constructor");
        assertEquals(name, service.getName(), "Incorrect name set in constructor");
        assertEquals(description, service.getDescription(), "Incorrect description set in constructor");
        assertEquals(duration, service.getDuration(), "Incorrect duration set in constructor");
        assertEquals(purpose, service.getPurpose(), "Incorrect purpose set in constructor");
        assertEquals(regularPrice, service.getRegularPrice(), "Incorrect regular price set in constructor");

    }

    @Test
    void validSkinServiceShouldHaveNoViolations() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "Hydration");
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(), "Expected no violations for valid SkinService, but got: " + violations);
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void blankPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "   ");
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for blank 'purpose', but got: " + violations);
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void nullPurposeShouldFailValidation() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, null);
        Set<ConstraintViolation<SkinService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "purpose"),
                "Expected violation for null 'purpose', but got: " + violations);
        List<SkinService> serviceList = SkinService.getSkinServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "Hydration");

        List<SkinService> listCopy = SkinService.getSkinServiceList();
        listCopy.clear();

        List<SkinService> originalList = SkinService.getSkinServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    @Test
    void serializationWorksCorrectly() throws IOException, ClassNotFoundException {
        SkinService service = new SkinService(1, "Facial", 50.0, "Deep cleansing", 60.0, "Hydration");
        SkinService service2 = new SkinService(2, "Facial", 60.0, "Deep cleansing", 70.0, "Hydration");

        SkinService.save();
        assertTrue(Files.exists(Path.of(SkinService.getExtentFile())));

        List<SkinService> originalList = List.of(service, service2);
        SkinService.clearExtent();
        SkinService.loadExtent();
        List<SkinService> savedList = SkinService.getSkinServiceList();

        assertEquals(savedList.size(), originalList.size());
        for (int i = 0; i < savedList.size(); i++) {
            SkinService savedService = savedList.get(i);
            SkinService originalService = originalList.get(i);

            // TODO: to avoid such block of code we have to override the equals() method in SkinService
            assertEquals(savedService.getId(), originalService.getId());
            assertEquals(savedService.getName(), originalService.getName());
            assertEquals(savedService.getDescription(), originalService.getDescription());
            assertEquals(savedService.getDuration(), originalService.getDuration());
            assertEquals(savedService.getPurpose(), originalService.getPurpose());
            assertEquals(savedService.getRegularPrice(), originalService.getRegularPrice());
        }
    }

    private boolean containsViolationFor(Set<ConstraintViolation<SkinService>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }


}

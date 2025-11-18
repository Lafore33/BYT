package com.example.byt.unit_tests.service_related;

import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsValuesCorrectly(){
        int id  = 1;
        String name = "Haircut";
        double regularPrice = 20.0;
        String description = "Basic haircut";
        double duration = 30.0;
        Service service = new Service(id, name, regularPrice, description, duration);
        assertEquals(id, service.getId(), "Incorrect ID set in constructor");
        assertEquals(name, service.getName(), "Incorrect name set in constructor");
        assertEquals(regularPrice, service.getRegularPrice(), "Incorrect regular price set in constructor");
        assertEquals(description, service.getDescription(), "Incorrect description set in constructor");
        assertEquals(duration, service.getDuration(), "Incorrect duration set in constructor");
    }

    @Test
    void validServiceShouldHaveNoViolationsAndShouldBeAddedToList() {
        Service service = new Service(1, "Haircut", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(), "Expected no validation errors for valid Service, but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertTrue(serviceList.contains(service), "The valid service should be added to the list");
    }

    @Test
    void negativeIdShouldFailValidation() {
        Service service = new Service(-1, "Haircut", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "id"),
                "Expected violation for field 'id' when negative, but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");

    }

    @Test
    void blankNameShouldFailValidation() {
        Service service = new Service(1, "   ", 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for blank 'name', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void nullNameShouldFailValidation() {
        Service service = new Service(1, null, 20.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for null 'name', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void negativeRegularPriceShouldFailValidation() {
        Service service = new Service(1, "Haircut", -10.0, "Basic haircut", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "regularPrice"),
                "Expected violation for negative 'regularPrice', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void blankDescriptionShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, "   ", 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for blank 'description', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void nullDescriptionShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, null, 30.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "description"),
                "Expected violation for null 'description', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void negativeDurationShouldFailValidation() {
        Service service = new Service(1, "Haircut", 20.0, "Basic haircut", -5.0);
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "duration"),
                "Expected violation for negative 'duration', but got: " + violations);
        List<Service> serviceList = Service.getServiceList();
        assertFalse(serviceList.contains(service), "The invalid service should not be added to the list");
    }

    @Test
    void getServiceListShouldReturnCopy() {
        Service service = new Service(1, "Haircut", 20.0, "Basic haircut", 30.0);

        List<Service> listCopy = Service.getServiceList();
        listCopy.clear();

        List<Service> originalList = Service.getServiceList();
        assertTrue(originalList.contains(service), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Service>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }



}

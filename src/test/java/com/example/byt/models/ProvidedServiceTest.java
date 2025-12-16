package com.example.byt.models;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.*;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceTest {

    private static Validator validator;
    private Customer customer;
    private Service service;
    private Master master;
    private Receptionist receptionist;
    private Appointment appointment;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        ProvidedService.clearExtent();
        Appointment.clearExtent();
        Customer.clearExtent();
        Service.clearExtent();
        Master.clearExtent();
        Receptionist.clearExtent();
        Certification.clearExtent();
        HistoryOfStatus.clearExtent();

        master = Worker.createMaster("Mike", "Smith", "444555666", LocalDate.of(1985, 3, 20), 5);
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30.0, Set.of(master));
        customer = Person.createCustomer("John", "Doe", "111222333", "john@example.com", LocalDate.of(1990, 5, 15));
        receptionist = Worker.createReceptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME);
        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        appointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo)).receptionist(receptionist).build();
    }

    @Test
    void constructorSetsValuesCorrectlyForFullProvidedService() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        int rating = 4;
        String comment = "This is a comment";
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .rating(rating)
                .comment(comment)
                .build();
        assertEquals(time, providedService.getTime(), "Incorrect time set in the constructor");
        assertEquals(rating, providedService.getRating(), "Incorrect rating set in the constructor");
        assertEquals(comment, providedService.getComment(), "Incorrect comment set in the constructor");
    }

    @Test
    void constructorSetsValuesCorrectlyForProvidedServiceWithOnlyRequiredFields() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        assertEquals(time, providedService.getTime(), "Incorrect time set in the constructor");
        assertNull(providedService.getRating(), "Incorrect rating set in the constructor");
        assertNull(providedService.getComment(), "Incorrect comment set in the constructor");
    }

    @Test
    void validProvidedServiceHasNoViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .rating(4)
                .comment("Great service")
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid ProvidedService");
        assertTrue(ProvidedService.getProvidedServiceList().contains(providedService),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void providedServiceWithoutOptionalFieldsIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for ProvidedService without optional fields");
        assertTrue(ProvidedService.getProvidedServiceList().contains(providedService),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void commentBlankStringIsConvertedToNull() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .comment("   ")
                .build();
        assertNull(providedService.getComment());
        assertTrue(ProvidedService.getProvidedServiceList().contains(providedService),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void timeNullProducesValidationError() {
        ProvidedService providedService = new ProvidedService.Builder(null, service, appointment, Set.of(master))
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(containsViolationFor(violations, "time"),
                "Expected violation for 'time' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(providedService),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingAboveMaxProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .rating(6)
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(containsViolationFor(violations, "rating"),
                "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(providedService),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingBelowMinProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .rating(0)
                .build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(containsViolationFor(violations, "rating"),
                "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(providedService),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void setRatingSetsValuesCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        providedService.setRating(5);
        assertEquals(5, providedService.getRating(), "Incorrect rating set in setRating");
    }

    @Test
    void setCommentSetsValuesCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        providedService.setComment("Great service");
        assertEquals("Great service", providedService.getComment(), "Incorrect comment set in setComment");
    }

    @Test
    void setRatingValidShouldHaveNoViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        providedService.setRating(5);
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(violations.isEmpty(), "Expected no violations");
    }

    @Test
    void setRatingInvalidShouldHaveViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        providedService.setRating(-1);
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        assertTrue(containsViolationFor(violations, "rating"), "Expected violations for invalid 'rating'");
    }

    @Test
    void setCommentBlankShouldStoreNullComment() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .build();
        providedService.setComment(" ");
        assertNull(providedService.getComment(), "Incorrect comment set in setComment");
    }

    @Test
    void getProvidedServiceListReturnsCopy() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService providedService = new ProvidedService.Builder(time, service, appointment, Set.of(master))
                .rating(3)
                .build();
        List<ProvidedService> listCopy = ProvidedService.getProvidedServiceList();
        listCopy.clear();
        List<ProvidedService> list = ProvidedService.getProvidedServiceList();
        assertTrue(list.contains(providedService), "The list should not be modified");
    }

    @Test
    void providedServiceWithoutMastersThrowsException() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        assertThrows(IllegalArgumentException.class, () ->
                        new ProvidedService.Builder(time, service, appointment, null).build(),
                "Expected IllegalArgumentException when masters is null"
        );
        assertThrows(IllegalArgumentException.class, () ->
                        new ProvidedService.Builder(time, service, appointment, Set.of()).build(),
                "Expected IllegalArgumentException when masters is empty"
        );
    }

    private boolean containsViolationFor(Set<ConstraintViolation<ProvidedService>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
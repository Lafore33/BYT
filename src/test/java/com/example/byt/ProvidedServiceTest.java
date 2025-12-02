package com.example.byt;

import com.example.byt.models.ProvidedService;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Master;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceTest {

    private static Validator validator;
    private Appointment testAppointment;
    private Service testService;
    private Set<Master> testMasters;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        ProvidedService.clearExtent();
        Appointment.clearExtent();
        Service.clearExtent();
        Master.clearExtent();
        testAppointment = new Appointment.Builder(LocalDate.of(2025, 11, 13)).build();
        testService = new Service(1, "Haircut", 50.0, "Basic haircut", 30.0);

        Master master = new Master("John", "Doe", "+1234567890", LocalDate.of(1990, 1, 1), 5);
        testMasters = new HashSet<>();
        testMasters.add(master);
    }

    @Test
    void constructorSetsValuesCorrectlyForFullProvidedService() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        int rating = 4;
        String comment = "This is a comment";

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .rating(rating)
                .comment(comment)
                .build();

        assertEquals(time, service.getTime(), "Incorrect time set in the constructor");
        assertEquals(rating, service.getRating(), "Incorrect rating set in the constructor");
        assertEquals(comment, service.getComment(), "Incorrect comment set in the constructor");
    }

    @Test
    void constructorSetsValuesCorrectlyForProvidedServiceWithOnlyTime() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();

        assertEquals(time, service.getTime(), "Incorrect time set in the constructor");
        assertNull(service.getRating(), "Incorrect rating set in the constructor");
        assertNull(service.getComment(), "Incorrect comment set in the constructor");
    }

    @Test
    void validProvidedServiceHasNoViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .rating(4)
                .comment("Great service")
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for valid ProvidedService");
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void providedServiceWithoutOptionalFieldsIsValid() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for ProvidedService without optional fields");
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void commentBlankStringIsConvertedToNull() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .comment("   ")
                .build();

        assertNull(service.getComment());
        assertTrue(ProvidedService.getProvidedServiceList().contains(service),
                "Valid ProvidedService should be added to extent");
    }

    @Test
    void timeNullProducesValidationError() {
        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, null)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "time"),
                "Expected violation for 'time' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(service),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingAboveMaxProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .rating(6)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "rating"),
                "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(service),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingBelowMinProducesValidationError() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .rating(0)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "rating"),
                "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(service),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void setRatingSetsValuesCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();
        service.setRating(5);

        assertEquals(5, service.getRating(), "Incorrect rating set in setRating");
    }

    @Test
    void setCommentSetsValuesCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();
        service.setComment("Great service");

        assertEquals("Great service", service.getComment(), "Incorrect comment set in setComment");
    }

    @Test
    void setRatingValidShouldHaveNoViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();
        service.setRating(5);

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(violations.isEmpty(), "Expected no violations");
    }

    @Test
    void setRatingInvalidShouldHaveViolations() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();
        service.setRating(-1);

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertTrue(containsViolationFor(violations, "rating"), "Expected violations for invalid 'rating'");
    }

    @Test
    void setCommentBlankShouldStoreNullComment() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .build();
        service.setComment(" ");

        assertNull(service.getComment(), "Incorrect comment set in setComment");
    }

    @Test
    void getProvidedServiceListReturnsCopy() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService service = new ProvidedService.Builder(testAppointment, testService, testMasters, time)
                .rating(3)
                .build();

        List<ProvidedService> listCopy = ProvidedService.getProvidedServiceList();
        listCopy.clear();

        List<ProvidedService> list = ProvidedService.getProvidedServiceList();
        assertTrue(list.contains(service), "The list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<ProvidedService>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
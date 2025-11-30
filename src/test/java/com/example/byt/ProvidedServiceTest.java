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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void clearExtent() {
        ProvidedService.clearExtent();
        Master.clearExtent();
        Service.clearExtent();
        Appointment.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectlyForFullProvidedService() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        int rating = 4;
        String comment = "This is a comment";

        ProvidedService ps = new ProvidedService.Builder(time)
                .rating(rating)
                .comment(comment)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertEquals(time, ps.getTime(), "Incorrect time set in the constructor");
        assertEquals(rating, ps.getRating(), "Incorrect rating set in the constructor");
        assertEquals(comment, ps.getComment(), "Incorrect comment set in the constructor");
    }

    @Test
    void constructorSetsValuesCorrectlyForProvidedServiceWithOnlyRequiredFields() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);

        ProvidedService ps = new ProvidedService.Builder(time)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertEquals(time, ps.getTime(), "Incorrect time set in the constructor");
        assertNull(ps.getRating(), "Rating should be null");
        assertNull(ps.getComment(), "Comment should be null");
    }

    @Test
    void validProvidedServiceHasNoViolations() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .rating(4)
                .comment("Great service")
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(ps);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
        assertTrue(ProvidedService.getProvidedServiceList().contains(ps), "Valid PS should be added to extent");
    }

    @Test
    void commentBlankStringIsConvertedToNull() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .comment("   ")
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertNull(ps.getComment());
    }

    @Test
    void setRatingSetsValuesCorrectly() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        ps.setRating(5);
        assertEquals(5, ps.getRating());
    }

    @Test
    void setCommentSetsValuesCorrectly() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        ps.setComment("Great service");
        assertEquals("Great service", ps.getComment());
    }

    @Test
    void setCommentBlankShouldStoreNullComment() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        ps.setComment(" ");
        assertNull(ps.getComment());
    }

    @Test
    void getProvidedServiceListReturnsCopy() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .rating(3)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        List<ProvidedService> listCopy = ProvidedService.getProvidedServiceList();
        listCopy.clear();
        List<ProvidedService> list = ProvidedService.getProvidedServiceList();
        assertTrue(list.contains(ps), "The list should not be modified");
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    void timeNullProducesValidationError() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(null)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(ps);
        assertTrue(containsViolationFor(violations, "time"), "Expected violation for 'time' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(ps),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingAboveMaxProducesValidationError() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .rating(6)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(ps);
        assertTrue(containsViolationFor(violations, "rating"), "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(ps),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void ratingBelowMinProducesValidationError() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .rating(0)
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(ps);
        assertTrue(containsViolationFor(violations, "rating"), "Expected violation for 'rating' field");
        assertFalse(ProvidedService.getProvidedServiceList().contains(ps),
                "Invalid ProvidedService should NOT be added to extent");
    }

    @Test
    void setRatingInvalidShouldHaveViolations() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 11, 13, 14, 30))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        ps.setRating(-1);

        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(ps);
        assertTrue(containsViolationFor(violations, "rating"), "Expected violations for invalid 'rating'");
    }


    @Test
    void builderCreatesAssociationsWithReverseConnections() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();
        assertTrue(ps.getMasters().contains(master1), "PS should contain master1");
        assertTrue(ps.getMasters().contains(master2), "PS should contain master2");
        assertEquals(service, ps.getService(), "PS should reference service");
        assertEquals(appointment, ps.getAppointment(), "PS should reference appointment");
        assertTrue(master1.getProvidedServices().contains(ps), "Master1 should contain PS (reverse)");
        assertTrue(master2.getProvidedServices().contains(ps), "Master2 should contain PS (reverse)");
        assertTrue(service.getProvidedServices().contains(ps), "Service should contain PS (reverse)");
        assertTrue(appointment.getProvidedServices().contains(ps), "Appointment should contain PS (reverse)");
    }

    @Test
    void addMasterCreatesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .service(service)
                .appointment(appointment)
                .build();

        ps.addMaster(master2);

        assertTrue(ps.getMasters().contains(master2), "PS should contain master2");
        assertTrue(master2.getProvidedServices().contains(ps), "Master2 should contain PS (reverse)");
    }

    @Test
    void removeMasterRemovesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();

        ps.removeMaster(master1);

        assertFalse(ps.getMasters().contains(master1), "PS should NOT contain master1");
        assertFalse(master1.getProvidedServices().contains(ps), "Master1 should NOT contain PS (reverse removed)");
        assertTrue(ps.getMasters().contains(master2), "PS should still contain master2");
        assertTrue(master2.getProvidedServices().contains(ps), "Master2 should still contain PS");
    }

    @Test
    void setServiceModifiesReverseConnections() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service1 = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Service service2 = new Service(2, "Pedicure", 60.0, "Professional pedicure", 75.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service1)
                .appointment(appointment)
                .build();

        ps.setService(service2);

        assertEquals(service2, ps.getService(), "PS should reference service2");
        assertTrue(service2.getProvidedServices().contains(ps), "Service2 should contain PS (reverse)");
        assertFalse(service1.getProvidedServices().contains(ps), "Service1 should NOT contain PS (reverse removed)");
    }

    @Test
    void setServiceSameServiceDoesNothing() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        int initialSize = service.getProvidedServices().size();

        ps.setService(service);

        assertEquals(service, ps.getService());
        assertEquals(initialSize, service.getProvidedServices().size());
        assertTrue(service.getProvidedServices().contains(ps));
    }

    @Test
    void setAppointmentModifiesReverseConnections() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 20)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment1)
                .build();

        ps.setAppointment(appointment2);

        assertEquals(appointment2, ps.getAppointment(), "PS should reference appointment2");
        assertTrue(appointment2.getProvidedServices().contains(ps), "Appointment2 should contain PS (reverse)");
        assertFalse(appointment1.getProvidedServices().contains(ps), "Appointment1 should NOT contain PS (reverse removed)");
    }

    @Test
    void setAppointmentSameAppointmentDoesNothing() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        int initialSize = appointment.getProvidedServices().size();

        ps.setAppointment(appointment);

        assertEquals(appointment, ps.getAppointment());
        assertEquals(initialSize, appointment.getProvidedServices().size());
        assertTrue(appointment.getProvidedServices().contains(ps));
    }

    @Test
    void builderWithoutMasterThrowsException() {
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        assertThrows(IllegalStateException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .service(service)
                        .appointment(appointment)
                        .build());
    }

    @Test
    void builderWithoutServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        assertThrows(IllegalStateException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .addMaster(master)
                        .appointment(appointment)
                        .build());
    }

    @Test
    void builderWithoutAppointmentThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);

        assertThrows(IllegalStateException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .addMaster(master)
                        .service(service)
                        .build());
    }

    @Test
    void builderWithNullMasterThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .addMaster(null));
    }

    @Test
    void builderWithNullServiceThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .service(null));
    }

    @Test
    void builderWithNullAppointmentThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .appointment(null));
    }

    @Test
    void builderExceedsMaxMastersThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Master master3 = new Master("Bob", "Johnson", "+48333333333", birthDate, 3);

        assertThrows(IllegalStateException.class, () ->
                new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                        .addMaster(master1)
                        .addMaster(master2)
                        .addMaster(master3));
    }

    @Test
    void addMasterExceedsMaxThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Master master3 = new Master("Bob", "Johnson", "+48333333333", birthDate, 3);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalStateException.class, () -> ps.addMaster(master3));
    }

    @Test
    void addDuplicateMasterThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> ps.addMaster(master));
    }

    @Test
    void addNullMasterThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> ps.addMaster(null));
    }

    @Test
    void removeMasterNullThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> ps.removeMaster(null));
    }

    @Test
    void removeMasterNotAssignedThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Master master3 = new Master("Bob", "Johnson", "+48333333333", birthDate, 3);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> ps.removeMaster(master3));
    }

    @Test
    void removeLastMasterThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalStateException.class, () -> ps.removeMaster(master));
    }
    @Test
    void setNullServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        assertThrows(IllegalArgumentException.class, () -> ps.setService(null));
    }

    @Test
    void setNullAppointmentThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        assertThrows(IllegalArgumentException.class, () -> ps.setAppointment(null));
    }
    @Test
    void getPriceBasedOnExperienceAndServicePrice() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();
        assertEquals(62.0, ps.getPrice());
    }

    @Test
    void getPriceRecalculatesAfterServiceChange() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service1 = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Service service2 = new Service(2, "Pedicure", 60.0, "Professional pedicure", 75.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service1)
                .appointment(appointment)
                .build();
        assertEquals(55.0, ps.getPrice());
        ps.setService(service2);
        assertEquals(65.0, ps.getPrice());
    }
    @Test
    void getMastersReturnsDefensiveCopy() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        Set<Master> copy = ps.getMasters();
        copy.clear();
        assertEquals(1, ps.getMasters().size(), "Original set should not be modified");
        assertTrue(ps.getMasters().contains(master), "Original set should still contain master");
    }
    private boolean containsViolationFor(Set<ConstraintViolation<ProvidedService>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
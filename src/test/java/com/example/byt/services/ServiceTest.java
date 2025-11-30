package com.example.byt.services;

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

public class ServiceTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @BeforeEach
    void clearExtent() {
        Service.clearExtent();
        ProvidedService.clearExtent();
        Master.clearExtent();
        Appointment.clearExtent();
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
    @Test
    void addProvidedServiceCreatesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        assertTrue(service.getProvidedServices().contains(ps),
                "Service should contain PS (direct check)");
        assertEquals(service, ps.getService(),
                "PS should reference service (reverse connection check)");
    }
    @Test
    void addProvidedServiceMovesFromOldService() {
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
        service2.addProvidedService(ps);
        assertTrue(service2.getProvidedServices().contains(ps),
                "Service2 should contain PS after move");
        assertEquals(service2, ps.getService(),
                "PS should reference service2 after move");
        assertFalse(service1.getProvidedServices().contains(ps),
                "Service1 should NOT contain PS after move");
    }
    @Test
    void moveMultipleProvidedServicesBetweenServicesPreservesIntegrity() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service1 = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Service service2 = new Service(2, "Pedicure", 60.0, "Professional pedicure", 75.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 16)).build();
        ProvidedService ps1 = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service1)
                .appointment(appointment1)
                .build();
        ProvidedService ps2 = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 16, 14, 0))
                .addMaster(master)
                .service(service1)
                .appointment(appointment2)
                .build();
        assertEquals(2, service1.getProvidedServices().size());
        assertEquals(0, service2.getProvidedServices().size());
        service2.addProvidedService(ps1);
        assertEquals(1, service1.getProvidedServices().size(),
                "Service1 should have 1 PS after moving ps1");
        assertEquals(1, service2.getProvidedServices().size(),
                "Service2 should have 1 PS after moving ps1");
        assertTrue(service1.getProvidedServices().contains(ps2),
                "Service1 should still contain ps2");
        assertTrue(service2.getProvidedServices().contains(ps1),
                "Service2 should contain ps1");
        assertEquals(service1, ps2.getService());
        assertEquals(service2, ps1.getService());
    }
    @Test
    void addNullProvidedServiceThrowsException() {
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);

        assertThrows(IllegalArgumentException.class, () -> service.addProvidedService(null));
    }

    @Test
    void addDuplicateProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();

        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.addProvidedService(ps));
    }

    @Test
    void getRatingReturnsZeroWhenNoProvidedServices() {
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);

        assertEquals(0.0, service.getRating());
    }

    @Test
    void getRatingCalculatesAverageOfAllRatings() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 16)).build();

        new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .rating(4)
                .addMaster(master)
                .service(service)
                .appointment(appointment1)
                .build();

        new ProvidedService.Builder(LocalDateTime.of(2025, 12, 16, 14, 0))
                .rating(5)
                .addMaster(master)
                .service(service)
                .appointment(appointment2)
                .build();

        assertEquals(4.5, service.getRating());
    }
    @Test
    void getRatingRecalculatesAfterProvidedServiceMoved() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service1 = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Service service2 = new Service(2, "Pedicure", 60.0, "Professional pedicure", 75.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 16)).build();
        ProvidedService ps1 = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .rating(4)
                .addMaster(master)
                .service(service1)
                .appointment(appointment1)
                .build();
        new ProvidedService.Builder(LocalDateTime.of(2025, 12, 16, 14, 0))
                .rating(2)
                .addMaster(master)
                .service(service1)
                .appointment(appointment2)
                .build();
        assertEquals(3.0, service1.getRating());
        service2.addProvidedService(ps1);
        assertEquals(2.0, service1.getRating());
        assertEquals(4.0, service2.getRating());
    }
    @Test
    void getProvidedServicesReturnsDefensiveCopy() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        Set<ProvidedService> copy = service.getProvidedServices();
        copy.clear();
        assertEquals(1, service.getProvidedServices().size(), "Original set should not be modified");
        assertTrue(service.getProvidedServices().contains(ps), "Original set should still contain ps");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Service>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }

}

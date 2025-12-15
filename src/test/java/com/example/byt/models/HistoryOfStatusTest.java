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

public class HistoryOfStatusTest {

    private static Validator validator;
    private Customer customer;
    private Appointment appointment;
    private Master master;
    private Service service;
    private Receptionist receptionist;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        HistoryOfStatus.clearExtent();
        Appointment.clearExtent();
        Customer.clearExtent();
        Service.clearExtent();
        Master.clearExtent();
        Receptionist.clearExtent();
        Certification.clearExtent();
        ProvidedService.clearExtent();

        master = Worker.createMaster("Mike", "Smith", "444555666", LocalDate.of(1985, 3, 20), 5).getMaster();
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30.0, Set.of(master));
        customer = Person.createCustomer("John", "Doe", "111222333", "john@example.com", LocalDate.of(1990, 5, 15));
        receptionist = Worker.createReceptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME).getReceptionist();
        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        appointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo)).receptionist(receptionist).build();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        AppointmentStatus status = AppointmentStatus.SCHEDULED;
        LocalDate date = LocalDate.of(2020, 1, 1);

        HistoryOfStatus historyOfStatus = new HistoryOfStatus(status, date, customer, appointment);
        assertEquals(status, historyOfStatus.getStatus(), "Incorrect status set in the constructor");
        assertEquals(date, historyOfStatus.getDateOfChangingStatus(), "Incorrect date set in the constructor");
    }

    @Test
    void validHistoryOfStatusShouldHaveNoViolations() {
        LocalDate date = LocalDate.now().minusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, date, customer, appointment);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid HistoryOfStatus, but got: " + violations);
        assertTrue(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Valid HistoryOfStatus should be added to extent");
    }

    @Test
    void nullStatusShouldFailValidation() {
        LocalDate date = LocalDate.now().minusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(null, date, customer, appointment);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertTrue(containsViolationFor(violations, "status"),
                "Expected violation for field 'status', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    @Test
    void nullDateShouldFailValidation() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, null, customer, appointment);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertTrue(containsViolationFor(violations, "dateOfChangingStatus"),
                "Expected violation for field 'dateOfChangingStatus', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    @Test
    void futureDateShouldFailValidation() {
        LocalDate future = LocalDate.now().plusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, future, customer, appointment);
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(history);
        assertFalse(violations.isEmpty(),
                "Expected validation violations for future 'dateOfChangingStatus', but got none");
        assertTrue(containsViolationFor(violations, "dateOfChangingStatus"),
                "Expected violation for field 'dateOfChangingStatus', but got: " + violations);
        assertFalse(HistoryOfStatus.getHistoryOfStatusList().contains(history),
                "Invalid HistoryOfStatus should NOT be added to extent");
    }

    @Test
    void getHistoryOfStatusListShouldReturnCopy() {
        LocalDate date = LocalDate.now().minusDays(1);
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.SCHEDULED, date, customer, appointment);
        List<HistoryOfStatus> listCopy = HistoryOfStatus.getHistoryOfStatusList();
        listCopy.clear();
        List<HistoryOfStatus> list = HistoryOfStatus.getHistoryOfStatusList();
        assertTrue(list.contains(history), "The list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<HistoryOfStatus>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
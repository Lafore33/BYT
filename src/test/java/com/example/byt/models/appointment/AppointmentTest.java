package com.example.byt.models.appointment;

import com.example.byt.models.ServiceInfo;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {
    private static Validator validator;
    private Customer customer;
    private Service service;
    private Master master;
    private Receptionist receptionist;
    private Set<ServiceInfo> serviceInfos;

    @BeforeEach
    void setUp() {
        Appointment.clearExtent();
        Customer.clearExtent();
        Service.clearExtent();
        Master.clearExtent();
        Receptionist.clearExtent();

        master = Worker.createMaster("Mike", "Smith", "444555666", LocalDate.of(1985, 3, 20), 5).getMaster();
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30.0, Set.of(master));
        customer = Person.createCustomer("John", "Doe", "111222333", "john@example.com", LocalDate.of(1990, 5, 15));
        receptionist = Worker.createReceptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME).getReceptionist();

        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        serviceInfos = Set.of(serviceInfo);
    }

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsValuesCorrectlyForFullAppointment() {
        LocalDate date = LocalDate.of(2025, 11, 1);
        List<String> notes = Arrays.asList("note1", "note2", "note3");
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .notes(notes)
                .paymentMethod(paymentMethod)
                .receptionist(receptionist)
                .build();
        assertEquals(date, appointment.getDate(), "Incorrect date set in the constructor");
        assertEquals(notes, appointment.getNotes(), "Incorrect notes set in the constructor");
        assertEquals(paymentMethod, appointment.getPaymentMethod(), "Incorrect payment method set in the constructor");
    }

    @Test
    void constructorSetsValuesCorrectlyForAppointmentWithOnlyRequiredFields() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        assertEquals(date, appointment.getDate(), "Incorrect date set in the constructor");
        assertNull(appointment.getPaymentMethod(), "Incorrect payment method set in the constructor");
        assertNull(appointment.getNotes(), "Incorrect notes set in the constructor");
    }

    @Test
    void validAppointmentPassesValidation() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid Appointment, but got: " + violations);
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }

    @Test
    void appointmentWithNullDateFailsValidation() {
        Appointment appointment = new Appointment.Builder(null, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(containsViolationFor(violations, "date"),
                "Expected violation for 'date' field");
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertFalse(appointmentList.contains(appointment),
                "Invalid appointment should NOT be added to extent");
    }

    @Test
    void appointmentWithNullServicesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(LocalDate.of(2025, 12, 1), customer, null)
                                .receptionist(receptionist)
                                .build(),
                "Expected IllegalArgumentException when services is null"
        );
    }

    @Test
    void appointmentWithEmptyServicesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(LocalDate.of(2025, 12, 1), customer, Set.of())
                                .receptionist(receptionist)
                                .build(),
                "Expected IllegalArgumentException when services is empty"
        );
    }

    @Test
    void appointmentNotesContainingNullThrowsException() {
        List<String> invalidNotes = Arrays.asList("Valid", null);
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(LocalDate.of(2025, 12, 1), customer, serviceInfos)
                                .notes(invalidNotes)
                                .receptionist(receptionist)
                                .build(),
                "Expected IllegalArgumentException when notes contains null element"
        );
    }

    @Test
    void appointmentNotesContainingBlankStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", "   ");
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(date, customer, serviceInfos)
                                .notes(invalidNotes)
                                .receptionist(receptionist)
                                .build(),
                "Expected IllegalArgumentException when notes contains blank string"
        );
    }

    @Test
    void setNotesWithValidValuesStoresNotesCorrectly() {
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 1), customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        List<String> validNotes = Arrays.asList("Note 1", "Note 2");
        appointment.setNotes(validNotes);
        assertEquals(validNotes, appointment.getNotes(), "Incorrect setNotes");
    }

    @Test
    void setNotesWithNotesContainingBlankStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        List<String> invalidNotes = Arrays.asList("Good", " ", "Bad");
        assertThrows(IllegalArgumentException.class, () ->
                        appointment.setNotes(invalidNotes),
                "Expected IllegalArgumentException when notes contains blank string"
        );
    }

    @Test
    void setNotesWithNotesContainingNullStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        List<String> invalidNotes = Arrays.asList("Good", null, "Bad");
        assertThrows(IllegalArgumentException.class, () ->
                        appointment.setNotes(invalidNotes),
                "Expected IllegalArgumentException when notes contains null string"
        );
    }

    @Test
    void setNotesToNullPasses() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .notes(List.of("Notes"))
                .receptionist(receptionist)
                .build();
        appointment.setNotes(null);
        assertNull(appointment.getNotes());
    }

    @Test
    void getNotesReturnsCopy() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> original = Arrays.asList("Note 1", "Note 2");
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .notes(original)
                .receptionist(receptionist)
                .build();
        List<String> returned = appointment.getNotes();
        assertNotSame(original, returned,
                "getNotes() should return a new copy, not the same list");
        assertEquals(original, returned,
                "Returned notes list should be equal to the original list");
    }

    @Test
    void setPaymentMethodStoresCorrectly() {
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 1), customer, serviceInfos)
                .notes(Arrays.asList("Note 1", "Note 2"))
                .receptionist(receptionist)
                .build();
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        appointment.setPaymentMethod(paymentMethod);
        assertEquals(paymentMethod, appointment.getPaymentMethod(), "Incorrect setPaymentMethod");
    }

    @Test
    void getAppointmentListShouldReturnCopy() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date, customer, serviceInfos)
                .receptionist(receptionist)
                .build();
        List<Appointment> listCopy = Appointment.getAppointmentList();
        listCopy.clear();
        List<Appointment> originalList = Appointment.getAppointmentList();
        assertTrue(originalList.contains(appointment),
                "The original list should not be modified when clearing the returned copy");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Appointment>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
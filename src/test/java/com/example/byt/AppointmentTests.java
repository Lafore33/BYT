package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTests {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validAppointmentWithOnlyDate() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        assertNotNull(appointment);
        assertEquals(date, appointment.getDate());
        assertNull(appointment.getNotes());
        assertNull(appointment.getPaymentMethod());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid Appointment, but got: " + violations);
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }

    @Test
    void appointmentWithNotesStoresThem() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> notes = Arrays.asList("First note", "Second note");
        Appointment appointment = new Appointment.Builder(date)
                .notes(notes)
                .build();
        assertNotNull(appointment.getNotes());
        assertEquals(2, appointment.getNotes().size());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for Appointment with valid notes, but got: " + violations);
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }

    @Test
    void appointmentNotesContainingNullThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", null);
        List<Appointment> before = Appointment.getAppointmentList();
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(date).notes(invalidNotes).build(),
                "Expected IllegalArgumentException when notes contains null element"
        );
        List<Appointment> after = Appointment.getAppointmentList();
        assertEquals(before.size(), after.size(),
                "Invalid appointment should not be added to extent");
    }

    @Test
    void appointmentNotesContainingEmptyStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", "   ");
        List<Appointment> before = Appointment.getAppointmentList();
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(date).notes(invalidNotes).build(),
                "Expected IllegalArgumentException when notes contains blank string"
        );
        List<Appointment> after = Appointment.getAppointmentList();
        assertEquals(before.size(), after.size(),
                "Invalid appointment should not be added to extent");
    }

    @Test
    void setNotesWithValidValuesStoresNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        List<String> validNotes = Arrays.asList("Note 1", "Note 2");
        appointment.setNotes(validNotes);
        assertEquals(2, appointment.getNotes().size());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Appointment should remain valid after setting valid notes");
    }

    @Test
    void setNotesWithInvalidValuesThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        List<String> invalidNotes = Arrays.asList("Good", "", "Bad");
        assertThrows(IllegalArgumentException.class, () ->
                        appointment.setNotes(invalidNotes),
                "Expected IllegalArgumentException when notes contains blank string"
        );
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Appointment should remain valid after failed setNotes()");
    }

    @Test
    void setNotesToNullClearsNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .notes(Arrays.asList("Note"))
                .build();
        appointment.setNotes(null);
        assertNull(appointment.getNotes());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Setting notes to null should not break validation");
    }

    @Test
    void getNotesReturnsCopy() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> original = Arrays.asList("Note 1", "Note 2");
        Appointment appointment = new Appointment.Builder(date)
                .notes(original)
                .build();
        List<String> returned = appointment.getNotes();
        assertNotSame(original, returned,
                "getNotes() should return a new copy, not the same list");
        assertEquals(original, returned,
                "Returned notes list should be equal to the original list");
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Appointment used in getNotes test should have no validation errors");
    }

    @Test
    void paymentMethodIsNullByDefault() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        assertNull(appointment.getPaymentMethod());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Default null paymentMethod should be valid");
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }

    @Test
    void paymentMethodCanBeSet() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .paymentMethod(PaymentMethod.CASH)
                .build();
        assertEquals(PaymentMethod.CASH, appointment.getPaymentMethod());
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Appointment with valid paymentMethod should have no validation errors");
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }

    @Test
    void appointmentWithNullDateHasValidationError() {
        Appointment appointment = new Appointment.Builder(null).build();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty(),
                "Appointment with null date should have validation errors");
        assertTrue(containsViolationFor(violations, "date"),
                "Expected violation for 'date' field");
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertFalse(appointmentList.contains(appointment),
                "Invalid appointment should NOT be added to extent");
    }

    @Test
    void getAppointmentListShouldReturnCopy() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
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

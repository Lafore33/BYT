package com.example.byt.unit_tests.appointment_related;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.appointment.PaymentMethod;
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
    void constructorSetsValuesCorrectlyForFullAppointment() {
        LocalDate date = LocalDate.of(2025, 11, 1);
        List<String> notes = Arrays.asList("note1", "note2", "note3");
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        Appointment appointment = new Appointment.Builder(date).notes(notes).paymentMethod(paymentMethod).build();
        assertEquals(date, appointment.getDate(), "Incorrect date set in the constructor");
        assertEquals(notes, appointment.getNotes(), "Incorrect notes set in the constructor");
        assertEquals(paymentMethod, appointment.getPaymentMethod(), "Incorrect payment method set in the constructor");
    }

    @Test
    void constructorSetsValuesCorrectlyForAppointmentWithOnlyDate() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        assertEquals(date, appointment.getDate(), "Incorrect date set in the constructor");
        assertNull(appointment.getPaymentMethod(), "Incorrect payment method set in the constructor");
        assertNull(appointment.getNotes(), "Incorrect notes set in the constructor");

    }

    @Test
    void validAppointmentPassesValidation() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid Appointment, but got: " + violations);
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertTrue(appointmentList.contains(appointment),
                "Valid appointment should be added to extent");
    }
    @Test
    void appointmentWithNullDateFailsValidation() {
        Appointment appointment = new Appointment.Builder(null).build();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(containsViolationFor(violations, "date"),
                "Expected violation for 'date' field");
        List<Appointment> appointmentList = Appointment.getAppointmentList();
        assertFalse(appointmentList.contains(appointment),
                "Invalid appointment should NOT be added to extent");
    }

    @Test
    void appointmentNotesContainingNullThrowsException() {
        List<String> invalidNotes = Arrays.asList("Valid", null);
        List<Appointment> before = Appointment.getAppointmentList();
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(LocalDate.of(2025, 12, 1))
                                .notes(invalidNotes).build(),
                "Expected IllegalArgumentException when notes contains null element"
        );
        List<Appointment> after = Appointment.getAppointmentList();
        assertEquals(before.size(), after.size(),
                "Invalid appointment should not be added to extent");
    }

    @Test
    void appointmentNotesContainingBlankStringThrowsException() {
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
    void setNotesWithValidValuesStoresNotesCorrectly() {
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 1)).build();
        List<String> validNotes = Arrays.asList("Note 1", "Note 2");
        appointment.setNotes(validNotes);
        assertEquals(validNotes, appointment.getNotes(), "Incorrect setNotes");
    }

    @Test
    void setNotesWithNotesContainingBlankStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        List<String> invalidNotes = Arrays.asList("Good", " ", "Bad");
        assertThrows(IllegalArgumentException.class, () ->
                        appointment.setNotes(invalidNotes),
                "Expected IllegalArgumentException when notes contains blank string"
        );
    }
    @Test
    void setNotesWithNotesContainingNullStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        List<String> invalidNotes = Arrays.asList("Good", null, "Bad");
        assertThrows(IllegalArgumentException.class, () ->
                        appointment.setNotes(invalidNotes),
                "Expected IllegalArgumentException when notes contains blank string"
        );
    }

    @Test
    void setNotesToNullPasses() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .notes(List.of("Notes"))
                .build();
        appointment.setNotes(null);
        assertNull(appointment.getNotes());
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
    }

    @Test
    void setPaymentMethodStoresCorrectly(){
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 1))
                .notes(Arrays.asList("Note 1", "Note 2"))
                .build();
        PaymentMethod paymentMethod = PaymentMethod.CARD;
        appointment.setPaymentMethod(paymentMethod);
        assertEquals(paymentMethod, appointment.getPaymentMethod(), "Incorrect setPaymentMethod");
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

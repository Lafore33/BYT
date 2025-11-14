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
        assertTrue(violations.isEmpty());
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
    }

    @Test
    void appointmentNotesContainingNullThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", null);

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment.Builder(date).notes(invalidNotes).build()
        );
    }

    @Test
    void appointmentNotesContainingEmptyStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", "   ");

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment.Builder(date).notes(invalidNotes).build()
        );
    }

    @Test
    void setNotesWithValidValuesStoresNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();

        List<String> validNotes = Arrays.asList("Note 1", "Note 2");
        appointment.setNotes(validNotes);

        assertEquals(2, appointment.getNotes().size());
    }

    @Test
    void setNotesWithInvalidValuesThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();

        List<String> invalidNotes = Arrays.asList("Good", "", "Bad");

        assertThrows(IllegalArgumentException.class, () ->
                appointment.setNotes(invalidNotes)
        );
    }

    @Test
    void setNotesToNullClearsNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .notes(Arrays.asList("Note"))
                .build();

        appointment.setNotes(null);

        assertNull(appointment.getNotes());
    }

    @Test
    void getNotesReturnsCopy() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> notes = Arrays.asList("Note 1", "Note 2");

        Appointment appointment = new Appointment.Builder(date)
                .notes(notes)
                .build();

        List<String> retrieved = appointment.getNotes();
        retrieved.clear();

        assertEquals(2, appointment.getNotes().size());
    }

    @Test
    void paymentMethodIsNullByDefault() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();

        assertNull(appointment.getPaymentMethod());
    }

    @Test
    void paymentMethodCanBeSet() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        assertEquals(PaymentMethod.CASH, appointment.getPaymentMethod());
    }

    @Test
    void appointmentWithNullDateHasValidationError() {
        Appointment appointment = new Appointment.Builder(null).build();

        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("date")));
    }
}

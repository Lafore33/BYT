package com.example.byt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentProvidedServiceTests {

    private final Validator validator;

    public AppointmentProvidedServiceTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testAppointmentCreation() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .paymentMethod(PaymentMethod.CARD)
                .build();
        assertNotNull(appointment);
        assertNull(appointment.getNotes());
        assertEquals(PaymentMethod.CARD, appointment.getPaymentMethod());
    }

    @Test
    void testAppointmentWithNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> notes = Arrays.asList("First note", "Second note");
        Appointment appointment = new Appointment.Builder(date)
                .notes(notes)
                .build();
        assertNotNull(appointment.getNotes());
        assertEquals(2, appointment.getNotes().size());
    }

    @Test
    void testAppointmentNotesNullValidation() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", null);
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment.Builder(date).notes(invalidNotes).build();
        });
    }

    @Test
    void testAppointmentNotesEmptyValidation() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", "   ");
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment.Builder(date).notes(invalidNotes).build();
        });
    }

    @Test
    void testAppointmentSetNotes() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date).build();
        List<String> validNotes = Arrays.asList("Note 1", "Note 2");
        appointment.setNotes(validNotes);
        assertEquals(2, appointment.getNotes().size());
        List<String> invalidNotes = Arrays.asList("Good", "", "Bad");
        assertThrows(IllegalArgumentException.class, () -> {
            appointment.setNotes(invalidNotes);
        });
    }

    @Test
    void testAppointmentSetNotesNull() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .notes(Arrays.asList("Note"))
                .build();
        appointment.setNotes(null);
        assertNull(appointment.getNotes());
    }

    @Test
    void testAppointmentNotesEncapsulation() {
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
    void testAppointmentPaymentMethodOptional() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment a1 = new Appointment.Builder(date).build();
        assertNull(a1.getPaymentMethod());
        Appointment a2 = new Appointment.Builder(date)
                .paymentMethod(PaymentMethod.CASH)
                .build();
        assertEquals(PaymentMethod.CASH, a2.getPaymentMethod());
    }

    @Test
    void testProvidedServiceCreation() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(5)
                .comment("Great service")
                .build();
        assertNotNull(service);
        assertEquals(5, service.getRating());
        assertEquals("Great service", service.getComment());
    }

    @Test
    void testProvidedServiceWithoutOptionals() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time).build();
        assertNotNull(service);
        assertNull(service.getRating());
        assertNull(service.getComment());
    }

    @Test
    void testProvidedServiceRatingOptional() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService s1 = new ProvidedService.Builder(time).build();
        assertNull(s1.getRating());
        ProvidedService s2 = new ProvidedService.Builder(time).rating(4).build();
        assertEquals(4, s2.getRating());
    }

    @Test
    void testProvidedServiceCommentOptional() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService s1 = new ProvidedService.Builder(time).build();
        assertNull(s1.getComment());
        ProvidedService s2 = new ProvidedService.Builder(time)
                .comment("Good")
                .build();
        assertEquals("Good", s2.getComment());
    }

    @Test
    void testProvidedServiceTimeRequired() {
        ProvidedService service = new ProvidedService.Builder(null).build();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(service);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("time")));
    }


    @Test
    void testProvidedServiceRatingRange() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService s1 = new ProvidedService.Builder(time).rating(0).build();
        assertEquals(0, s1.getRating());
        ProvidedService s2 = new ProvidedService.Builder(time).rating(5).build();
        assertEquals(5, s2.getRating());
    }

    @Test
    void testAppointmentDateAttribute() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        Appointment appointment = new Appointment.Builder(date)
                .paymentMethod(PaymentMethod.CARD)
                .build();
        assertEquals(date, appointment.getDate());
    }


    @Test
    void testProvidedServiceTimeAttribute() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 13, 14, 30);
        ProvidedService service = new ProvidedService.Builder(time)
                .rating(3)
                .comment("ok")
                .build();
        assertEquals(time, service.getTime());
    }
}
package com.example.byt.appointment;

import com.example.byt.models.ProvidedService;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.appointment.PaymentMethod;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

    private static Validator validator;

    @BeforeEach
    void clearExtent() {
        Appointment.clearExtent();
        ProvidedService.clearExtent();
        Master.clearExtent();
        Service.clearExtent();
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
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(LocalDate.of(2025, 12, 1))
                                .notes(invalidNotes).build(),
                "Expected IllegalArgumentException when notes contains null element"
        );
        List<Appointment> list = Appointment.getAppointmentList();
        assertTrue(list.isEmpty(),
                "Invalid appointment should not be added to extent");
    }

    @Test
    void appointmentNotesContainingBlankStringThrowsException() {
        LocalDate date = LocalDate.of(2025, 12, 1);
        List<String> invalidNotes = Arrays.asList("Valid", "   ");
        assertThrows(IllegalArgumentException.class, () ->
                        new Appointment.Builder(date).notes(invalidNotes).build(),
                "Expected IllegalArgumentException when notes contains blank string"
        );
        List<Appointment> list = Appointment.getAppointmentList();
        assertTrue(list.isEmpty(),
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
    @Test
    void addProvidedServiceCreatesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 16)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment1)
                .build();
        assertTrue(appointment1.getProvidedServices().contains(ps), "Appointment1 should contain PS");
        assertEquals(appointment1, ps.getAppointment(), "PS should reference appointment1");
        appointment2.addProvidedService(ps);
        assertTrue(appointment2.getProvidedServices().contains(ps), "Appointment2 should contain PS");
        assertEquals(appointment2, ps.getAppointment(), "PS should reference appointment2 (reverse)");
        assertFalse(appointment1.getProvidedServices().contains(ps), "Appointment1 should NOT contain PS anymore");
    }

    @Test
    void moveProvidedServiceBetweenAppointmentsPreservesIntegrity() {
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
        ProvidedService ps2 = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 15, 0))
                .addMaster(master)
                .service(service2)
                .appointment(appointment1)
                .build();
        assertEquals(2, appointment1.getProvidedServices().size(), "Appointment1 should have 2 PS initially");
        assertEquals(0, appointment2.getProvidedServices().size(), "Appointment2 should be empty initially");
        appointment2.addProvidedService(ps1);
        assertEquals(1, appointment1.getProvidedServices().size(), "Appointment1 should have 1 PS after move");
        assertEquals(1, appointment2.getProvidedServices().size(), "Appointment2 should have 1 PS after move");
        assertTrue(appointment1.getProvidedServices().contains(ps2), "Appointment1 should still contain ps2");
        assertFalse(appointment1.getProvidedServices().contains(ps1), "Appointment1 should NOT contain ps1");
        assertTrue(appointment2.getProvidedServices().contains(ps1), "Appointment2 should contain ps1");
        assertEquals(appointment2, ps1.getAppointment(), "ps1 should reference appointment2");
        assertEquals(appointment1, ps2.getAppointment(), "ps2 should still reference appointment1");
    }
    @Test
    void addNullProvidedServiceThrowsException() {
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        assertThrows(IllegalArgumentException.class, () -> appointment.addProvidedService(null));
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
        assertThrows(IllegalArgumentException.class, () -> appointment.addProvidedService(ps));
    }
    @Test
    void getTotalPriceReturnsZeroWhenNoProvidedServices() {
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        assertEquals(0.0, appointment.getTotalPrice());
    }

    @Test
    void getTotalPriceCalculatesSumOfAllServicePrices() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service1 = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Service service2 = new Service(2, "Pedicure", 60.0, "Professional pedicure", 75.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .service(service1)
                .appointment(appointment)
                .build();
        new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 15, 0))
                .addMaster(master2)
                .service(service2)
                .appointment(appointment)
                .build();
        assertEquals(122.0, appointment.getTotalPrice());
    }

    @Test
    void getTotalPriceRecalculatesAfterProvidedServiceMoved() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment1 = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        Appointment appointment2 = new Appointment.Builder(LocalDate.of(2025, 12, 16)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment1)
                .build();
        assertEquals(55.0, appointment1.getTotalPrice(), "Appointment1 totalPrice before move");
        assertEquals(0.0, appointment2.getTotalPrice(), "Appointment2 totalPrice before move");
        appointment2.addProvidedService(ps);
        assertEquals(0.0, appointment1.getTotalPrice(), "Appointment1 totalPrice after move");
        assertEquals(55.0, appointment2.getTotalPrice(), "Appointment2 totalPrice after move");
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
        Set<ProvidedService> copy = appointment.getProvidedServices();
        copy.clear();
        assertEquals(1, appointment.getProvidedServices().size(), "Original set should not be modified");
        assertTrue(appointment.getProvidedServices().contains(ps), "Original set should still contain ps");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Appointment>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
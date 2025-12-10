package com.example.byt.associations;

import com.example.byt.models.AppointmentStatus;
import com.example.byt.models.HistoryOfStatus;
import com.example.byt.models.ServiceInfo;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Customer;
import com.example.byt.models.person.Master;
import com.example.byt.models.person.Receptionist;
import com.example.byt.models.person.WorkType;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class HistoryOfStatusAppointmentAssociationTest {

    private Customer customer;
    private Master master;
    private Service service;
    private Receptionist receptionist;
    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        HistoryOfStatus.clearExtent();
        Customer.clearExtent();
        Master.clearExtent();
        Service.clearExtent();
        Receptionist.clearExtent();
        Appointment.clearExtent();

        customer = new Customer("John", "Doe", "123456789", LocalDate.of(1990, 1, 1));
        master = new Master("Mike", "Johnson", "555555555", LocalDate.of(1980, 3, 20), 5);
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30, Set.of(master));
        receptionist = new Receptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME);

        ServiceInfo serviceInfo1 = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        appointment1 = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo1))
                .receptionist(receptionist)
                .build();

        ServiceInfo serviceInfo2 = new ServiceInfo(service, LocalDateTime.now().plusHours(2), Set.of(master));
        appointment2 = new Appointment.Builder(LocalDate.now().plusDays(1), customer, Set.of(serviceInfo2))
                .receptionist(receptionist)
                .build();
    }

    @Test
    void constructorAddsAssociationsBothSides() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer, appointment1);

        assertTrue(appointment1.getHistoryOfStatuses().contains(history));
        assertEquals(appointment1, history.getAppointment());
    }

    @Test
    void constructorWithNullAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer, null));
    }

    @Test
    void addHistoryUpdatesBothSides() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer, appointment1);

        appointment2.addHistory(history);

        assertTrue(appointment2.getHistoryOfStatuses().contains(history));
    }

    @Test
    void addNullHistoryThrowsException() {
        assertThrows(NullPointerException.class, () -> appointment1.addHistory(null));
    }

    @Test
    void addDuplicateHistoryDoesNotDuplicate() {
        HistoryOfStatus history = appointment1.getHistoryOfStatuses().iterator().next();
        int sizeBefore = appointment1.getHistoryOfStatuses().size();

        appointment1.addHistory(history);

        assertEquals(sizeBefore, appointment1.getHistoryOfStatuses().size());
    }

    @Test
    void addSameAppointmentDoesNotDuplicate() {
        HistoryOfStatus history = appointment1.getHistoryOfStatuses().iterator().next();
        int sizeBefore = appointment1.getHistoryOfStatuses().size();

        history.addAppointment(appointment1);

        assertEquals(sizeBefore, appointment1.getHistoryOfStatuses().size());
    }

    @Test
    void appointmentCanHaveMultipleHistoryEntries() {
        new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer, appointment1);
        new HistoryOfStatus(AppointmentStatus.PAID, LocalDate.now(), customer, appointment1);

        assertEquals(3, appointment1.getHistoryOfStatuses().size());
    }

    @Test
    void getHistoryOfStatusesReturnsCopy() {
        Set<HistoryOfStatus> histories = appointment1.getHistoryOfStatuses();
        histories.clear();

        assertFalse(appointment1.getHistoryOfStatuses().isEmpty());
    }
}
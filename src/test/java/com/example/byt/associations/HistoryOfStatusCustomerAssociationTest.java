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
public class HistoryOfStatusCustomerAssociationTest {

    private Customer customer1;
    private Customer customer2;
    private Master master;
    private Service service;
    private Receptionist receptionist;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        HistoryOfStatus.clearExtent();
        Customer.clearExtent();
        Master.clearExtent();
        Service.clearExtent();
        Receptionist.clearExtent();
        Appointment.clearExtent();

        customer1 = new Customer("John", "Doe", "123456789", LocalDate.of(1990, 1, 1));
        customer2 = new Customer("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 15));
        master = new Master("Mike", "Johnson", "555555555", LocalDate.of(1980, 3, 20), 5);
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30, Set.of(master));
        receptionist = new Receptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME);

        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        appointment = new Appointment.Builder(LocalDate.now(), customer1, Set.of(serviceInfo))
                .receptionist(receptionist)
                .build();
    }

    @Test
    void constructorAddsAssociationsBothSides() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer2, appointment);

        assertTrue(customer2.getHistoryOfStatuses().contains(history));
        assertEquals(customer2, history.getCustomer());
    }

    @Test
    void constructorWithNullCustomerThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), null, appointment));
    }

    @Test
    void addHistoryUpdatesBothSides() {
        HistoryOfStatus history = new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer1, appointment);

        customer2.addHistory(history);

        assertTrue(customer2.getHistoryOfStatuses().contains(history));
    }

    @Test
    void addNullHistoryThrowsException() {
        assertThrows(NullPointerException.class, () -> customer1.addHistory(null));
    }

    @Test
    void addDuplicateHistoryDoesNotDuplicate() {
        HistoryOfStatus history = customer1.getHistoryOfStatuses().iterator().next();
        int sizeBefore = customer1.getHistoryOfStatuses().size();

        customer1.addHistory(history);

        assertEquals(sizeBefore, customer1.getHistoryOfStatuses().size());
    }

    @Test
    void addSameCustomerDoesNotDuplicate() {
        HistoryOfStatus history = customer1.getHistoryOfStatuses().iterator().next();
        int sizeBefore = customer1.getHistoryOfStatuses().size();

        history.addCustomer(customer1);

        assertEquals(sizeBefore, customer1.getHistoryOfStatuses().size());
    }

    @Test
    void customerCanHaveMultipleHistoryEntries() {
        new HistoryOfStatus(AppointmentStatus.COMPLETED, LocalDate.now(), customer1, appointment);
        new HistoryOfStatus(AppointmentStatus.PAID, LocalDate.now(), customer1, appointment);

        assertEquals(3, customer1.getHistoryOfStatuses().size());
    }

    @Test
    void customerCanHaveZeroHistoryEntries() {
        assertTrue(customer2.getHistoryOfStatuses().isEmpty());
    }

    @Test
    void getHistoryOfStatusesReturnsCopy() {
        Set<HistoryOfStatus> histories = customer1.getHistoryOfStatuses();
        histories.clear();

        assertFalse(customer1.getHistoryOfStatuses().isEmpty());
    }
}
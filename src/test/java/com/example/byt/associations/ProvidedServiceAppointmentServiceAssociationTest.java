package com.example.byt.associations;

import com.example.byt.models.ProvidedService;
import com.example.byt.models.ServiceInfo;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.*;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceAppointmentServiceAssociationTest {

    private Customer customer;
    private Master master1;
    private Master master2;
    private Service service1;
    private Service service2;
    private Receptionist receptionist;
    private Appointment appointment1;
    private Appointment appointment2;
    private ProvidedService providedService;

    @BeforeEach
    void setUp() {
        ProvidedService.clearExtent();
        Customer.clearExtent();
        Master.clearExtent();
        Service.clearExtent();
        Receptionist.clearExtent();
        Appointment.clearExtent();

        customer = Person.createCustomer("John", "Doe", "123456789", LocalDate.of(1990, 1, 1));
        master1 = Worker.createMaster("Mike", "Johnson", "555555555", LocalDate.of(1980, 3, 20), 5).getMaster();
        master2 = Worker.createMaster("Sarah", "Williams", "666666666", LocalDate.of(1985, 6, 15), 4).getMaster();
        service1 = new Service(1, "Haircut", 50.0, "Basic haircut", 30, Set.of(master1, master2));
        service2 = new Service(2, "Coloring", 100.0, "Hair coloring", 60, Set.of(master1, master2));
        receptionist = Worker.createReceptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME).getReceptionist();

        ServiceInfo serviceInfo1 = new ServiceInfo(service1, LocalDateTime.now(), Set.of(master1));
        appointment1 = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo1))
                .receptionist(receptionist)
                .build();
        providedService = appointment1.getProvidedServices().iterator().next();

        ServiceInfo serviceInfo2 = new ServiceInfo(service2, LocalDateTime.now().plusHours(2), Set.of(master2));
        appointment2 = new Appointment.Builder(LocalDate.now().plusDays(1), customer, Set.of(serviceInfo2))
                .receptionist(receptionist)
                .build();
    }

    @Test
    void constructorAddsAppointmentAssociationBothSides() {
        assertTrue(appointment1.getProvidedServices().contains(providedService));
        assertEquals(appointment1, providedService.getAppointment());
    }

    @Test
    void constructorWithNullAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), service1, null, Set.of(master1)).build());
    }

    @Test
    void addProvidedServiceToAppointmentUpdatesBothSides() {
        appointment2.addProvidedService(providedService);

        assertTrue(appointment2.getProvidedServices().contains(providedService));
    }

    @Test
    void addNullProvidedServiceToAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () -> appointment1.addProvidedService(null));
    }

    @Test
    void addDuplicateProvidedServiceToAppointmentDoesNotDuplicate() {
        int sizeBefore = appointment1.getProvidedServices().size();

        appointment1.addProvidedService(providedService);

        assertEquals(sizeBefore, appointment1.getProvidedServices().size());
    }

    @Test
    void addSameAppointmentDoesNotDuplicate() {
        int sizeBefore = appointment1.getProvidedServices().size();

        providedService.addAppointment(appointment1);

        assertEquals(sizeBefore, appointment1.getProvidedServices().size());
    }

    @Test
    void addNullAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.addAppointment(null));
    }

    @Test
    void removeAppointmentUpdatesBothSides() {
        Appointment oldAppointment = providedService.getAppointment();
        Service oldService = providedService.getService();

        providedService.removeAppointment(oldAppointment);

        assertNull(providedService.getAppointment());
        assertNull(providedService.getService());
        assertFalse(oldAppointment.getProvidedServices().contains(providedService));
        assertFalse(oldService.getProvidedServices().contains(providedService));
    }

    @Test
    void removeNullAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.removeAppointment(null));
    }

    @Test
    void removeAppointmentNotLinkedDoesNothing() {
        providedService.removeAppointment(appointment2);

        assertEquals(appointment1, providedService.getAppointment());
    }

    @Test
    void removeProvidedServiceFromAppointmentUpdatesBothSides() {
        appointment1.removeProvidedService(providedService);

        assertFalse(appointment1.getProvidedServices().contains(providedService));
        assertNull(providedService.getAppointment());
    }

    @Test
    void removeNullProvidedServiceFromAppointmentThrowsException() {
        assertThrows(NullPointerException.class, () -> appointment1.removeProvidedService(null));
    }

    @Test
    void removeProvidedServiceNotInAppointmentDoesNothing() {
        ProvidedService psFromAppointment2 = appointment2.getProvidedServices().iterator().next();
        int sizeBefore = appointment1.getProvidedServices().size();

        appointment1.removeProvidedService(psFromAppointment2);

        assertEquals(sizeBefore, appointment1.getProvidedServices().size());
    }

    @Test
    void appointmentCanHaveMultipleProvidedServices() {
        ServiceInfo serviceInfo1 = new ServiceInfo(service1, LocalDateTime.now(), Set.of(master1));
        ServiceInfo serviceInfo2 = new ServiceInfo(service2, LocalDateTime.now().plusHours(1), Set.of(master2));
        Appointment appointment = new Appointment.Builder(LocalDate.now().plusDays(5), customer, Set.of(serviceInfo1, serviceInfo2))
                .receptionist(receptionist)
                .build();

        assertEquals(2, appointment.getProvidedServices().size());
    }

    @Test
    void getAppointmentProvidedServicesReturnsCopy() {
        Set<ProvidedService> services = appointment1.getProvidedServices();
        services.clear();

        assertFalse(appointment1.getProvidedServices().isEmpty());
    }

    @Test
    void constructorAddsServiceAssociationBothSides() {
        assertTrue(service1.getProvidedServices().contains(providedService));
        assertEquals(service1, providedService.getService());
    }

    @Test
    void constructorWithNullServiceThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), null, appointment1, Set.of(master1)).build());
    }

    @Test
    void addProvidedServiceToServiceUpdatesBothSides() {
        service2.addProvidedService(providedService);

        assertTrue(service2.getProvidedServices().contains(providedService));
    }

    @Test
    void addNullProvidedServiceToServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> service1.addProvidedService(null));
    }

    @Test
    void addDuplicateProvidedServiceToServiceDoesNotDuplicate() {
        int sizeBefore = service1.getProvidedServices().size();

        service1.addProvidedService(providedService);

        assertEquals(sizeBefore, service1.getProvidedServices().size());
    }

    @Test
    void addSameServiceDoesNotDuplicate() {
        int sizeBefore = service1.getProvidedServices().size();

        providedService.addService(service1);

        assertEquals(sizeBefore, service1.getProvidedServices().size());
    }

    @Test
    void addNullServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.addService(null));
    }

    @Test
    void removeServiceUpdatesBothSides() {
        Service oldService = providedService.getService();
        Appointment oldAppointment = providedService.getAppointment();

        providedService.removeService(oldService);

        assertNull(providedService.getService());
        assertNull(providedService.getAppointment());
        assertFalse(oldService.getProvidedServices().contains(providedService));
        assertFalse(oldAppointment.getProvidedServices().contains(providedService));
    }

    @Test
    void removeNullServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.removeService(null));
    }

    @Test
    void removeServiceNotLinkedDoesNothing() {
        providedService.removeService(service2);

        assertEquals(service1, providedService.getService());
    }

    @Test
    void removeProvidedServiceFromServiceUpdatesBothSides() {
        service1.removeProvidedService(providedService);

        assertFalse(service1.getProvidedServices().contains(providedService));
    }

    @Test
    void removeNullProvidedServiceFromServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> service1.removeProvidedService(null));
    }

    @Test
    void removeProvidedServiceNotInServiceDoesNothing() {
        int sizeBefore = service2.getProvidedServices().size();

        service2.removeProvidedService(providedService);

        assertEquals(sizeBefore, service2.getProvidedServices().size());
    }

    @Test
    void serviceCanHaveMultipleProvidedServices() {
        ServiceInfo serviceInfo2 = new ServiceInfo(service1, LocalDateTime.now().plusHours(2), Set.of(master2));
        new Appointment.Builder(LocalDate.now().plusDays(1), customer, Set.of(serviceInfo2))
                .receptionist(receptionist)
                .build();

        assertEquals(2, service1.getProvidedServices().size());
    }

    @Test
    void serviceCanHaveZeroProvidedServices() {
        Service newService = new Service(3, "New Service", 30.0, "New", 20, Set.of(master1));
        assertTrue(newService.getProvidedServices().isEmpty());
    }

    @Test
    void getServiceProvidedServicesReturnsCopy() {
        Set<ProvidedService> services = service1.getProvidedServices();
        services.clear();

        assertFalse(service1.getProvidedServices().isEmpty());
    }
}
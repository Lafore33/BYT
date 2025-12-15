package com.example.byt.associations;

import com.example.byt.models.ProvidedService;
import com.example.byt.models.ServiceInfo;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.*;
import com.example.byt.models.services.FourHandsService;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProvidedServiceMasterAssociationTest {

    private Customer customer;
    private Master master1;
    private Master master2;
    private Master master3;
    private Master masterNotSpecialized;
    private Service service1;
    private Service service2;
    private FourHandsService fourHandsService;
    private Receptionist receptionist;
    private Appointment appointment;
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
        master1 = new Master("Mike", "Johnson", "555555555", LocalDate.of(1980, 3, 20), 5);
        master2 = new Master("Sarah", "Williams", "666666666", LocalDate.of(1985, 6, 15), 4);
        master3 = new Master("Tom", "Davis", "777777777", LocalDate.of(1982, 9, 25), 3);
        masterNotSpecialized = new Master("Bob", "Wilson", "888888888", LocalDate.of(1988, 12, 1), 2);

        service1 = new Service(1, "Haircut", 50.0, "Basic haircut", 30, Set.of(master1, master2, master3));
        service2 = new Service(2, "Coloring", 100.0, "Hair coloring", 60, Set.of(master1, master2));
        fourHandsService = Service.createFourHandsService(3, "Four Hands Massage", 200.0, "Luxury massage", 90, Set.of(master1, master2, master3), false);
        receptionist = new Receptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME);

        ServiceInfo serviceInfo = new ServiceInfo(service1, LocalDateTime.now(), Set.of(master1));
        appointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo))
                .receptionist(receptionist)
                .build();
        providedService = appointment.getProvidedServices().iterator().next();
    }

    @Test
    void constructorAddsAssociationsBothSides() {
        assertTrue(providedService.getCompletedByMasters().contains(master1));
        assertTrue(master1.getCompletedServices().contains(providedService));
    }

    @Test
    void constructorWithNullMastersThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), service1, appointment, null).build());
    }

    @Test
    void constructorWithEmptyMastersThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), service1, appointment, Set.of()).build());
    }

    @Test
    void constructorWithSetContainingNullMasterThrowsException() {
        Set<Master> masters = new HashSet<>();
        masters.add(null);
        masters.add(master1);
        assertThrows(NullPointerException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), service1, appointment, masters).build());
    }

    @Test
    void constructorWithThreeMastersThrowsException() {
        ServiceInfo serviceInfo = new ServiceInfo(service1, LocalDateTime.now(), Set.of(master1, master2, master3));

        assertThrows(IllegalStateException.class, () ->
                new Appointment.Builder(LocalDate.now().plusDays(10), customer, Set.of(serviceInfo))
                        .receptionist(receptionist)
                        .build());
    }

    @Test
    void constructorWithMasterNotSpecializedThrowsException() {
        ServiceInfo serviceInfo = new ServiceInfo(service1, LocalDateTime.now(), Set.of(masterNotSpecialized));

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment.Builder(LocalDate.now().plusDays(10), customer, Set.of(serviceInfo))
                        .receptionist(receptionist)
                        .build());
    }

    @Test
    void addMasterUpdatesBothSides() {
        providedService.addMaster(master2);

        assertTrue(providedService.getCompletedByMasters().contains(master2));
        assertTrue(master2.getCompletedServices().contains(providedService));
    }

    @Test
    void addNullMasterThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.addMaster(null));
    }

    @Test
    void addMasterWhenAlreadyTwoThrowsException() {
        providedService.addMaster(master2);

        assertThrows(IllegalStateException.class, () -> providedService.addMaster(master3));
    }

    @Test
    void addDuplicateMasterDoesNotDuplicate() {
        int sizeBefore = providedService.getCompletedByMasters().size();

        providedService.addMaster(master1);

        assertEquals(sizeBefore, providedService.getCompletedByMasters().size());
    }

    @Test
    void addMasterNotSpecializedThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> providedService.addMaster(masterNotSpecialized));
    }

    @Test
    void addCompletedServiceUpdatesBothSides() {
        master2.addCompletedService(providedService);

        assertTrue(master2.getCompletedServices().contains(providedService));
        assertTrue(providedService.getCompletedByMasters().contains(master2));
    }

    @Test
    void addNullCompletedServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> master1.addCompletedService(null));
    }

    @Test
    void addDuplicateCompletedServiceDoesNotDuplicate() {
        int sizeBefore = master1.getCompletedServices().size();

        master1.addCompletedService(providedService);

        assertEquals(sizeBefore, master1.getCompletedServices().size());
    }

    @Test
    void removeMasterUpdatesBothSides() {
        providedService.addMaster(master2);

        providedService.removeMaster(master2);

        assertFalse(providedService.getCompletedByMasters().contains(master2));
        assertFalse(master2.getCompletedServices().contains(providedService));
    }

    @Test
    void removeNullMasterThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.removeMaster(null));
    }

    @Test
    void removeLastMasterThrowsException() {
        assertThrows(IllegalStateException.class, () -> providedService.removeMaster(master1));
    }

    @Test
    void removeMasterNotInSetDoesNothing() {
        int sizeBefore = providedService.getCompletedByMasters().size();

        providedService.removeMaster(master2);

        assertEquals(sizeBefore, providedService.getCompletedByMasters().size());
    }

    @Test
    void removeCompletedServiceUpdatesBothSides() {
        providedService.addMaster(master2);

        master2.removeCompletedService(providedService);

        assertFalse(master2.getCompletedServices().contains(providedService));
        assertFalse(providedService.getCompletedByMasters().contains(master2));
    }

    @Test
    void removeNullCompletedServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> master1.removeCompletedService(null));
    }

    @Test
    void removeCompletedServiceNotInSetDoesNothing() {
        ProvidedService otherPs = appointment.getProvidedServices().iterator().next();
        int sizeBefore = master2.getCompletedServices().size();

        master2.removeCompletedService(otherPs);

        assertEquals(sizeBefore, master2.getCompletedServices().size());
    }

    @Test
    void fourHandsServiceWithOneMasterThrowsException() {
        ServiceInfo serviceInfo = new ServiceInfo(fourHandsService, LocalDateTime.now(), Set.of(master1));

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment.Builder(LocalDate.now().plusDays(10), customer, Set.of(serviceInfo))
                        .receptionist(receptionist)
                        .build());
    }

    @Test
    void fourHandsServiceWithTwoMastersIsValid() {
        ServiceInfo serviceInfo = new ServiceInfo(fourHandsService, LocalDateTime.now(), Set.of(master1, master2));
        Appointment fourHandsAppointment = new Appointment.Builder(LocalDate.now().plusDays(10), customer, Set.of(serviceInfo))
                .receptionist(receptionist)
                .build();

        ProvidedService ps = fourHandsAppointment.getProvidedServices().iterator().next();
        assertEquals(2, ps.getCompletedByMasters().size());
    }

    @Test
    void masterCanHaveMultipleCompletedServices() {
        ServiceInfo serviceInfo2 = new ServiceInfo(service2, LocalDateTime.now().plusHours(2), Set.of(master1));
        new Appointment.Builder(LocalDate.now().plusDays(1), customer, Set.of(serviceInfo2))
                .receptionist(receptionist)
                .build();

        assertEquals(2, master1.getCompletedServices().size());
    }

    @Test
    void masterCanHaveZeroCompletedServices() {
        assertTrue(master3.getCompletedServices().isEmpty());
    }

    @Test
    void getCompletedByMastersReturnsCopy() {
        Set<Master> masters = providedService.getCompletedByMasters();
        masters.clear();

        assertFalse(providedService.getCompletedByMasters().isEmpty());
    }

    @Test
    void getCompletedServicesReturnsCopy() {
        Set<ProvidedService> services = master1.getCompletedServices();
        services.clear();

        assertFalse(master1.getCompletedServices().isEmpty());
    }
}
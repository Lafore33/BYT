package com.example.byt.associations;

import com.example.byt.models.ProvidedService;
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

public class ProvidedServiceServiceAssociationTest {

    private Customer customer;
    private Master master1;
    private Master master2;
    private Service service1;
    private Service service2;
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

        customer = new Customer("John", "Doe", "123456789", LocalDate.of(1990, 1, 1));
        master1 = new Master("Mike", "Johnson", "555555555", LocalDate.of(1980, 3, 20), 5);
        master2 = new Master("Sarah", "Williams", "666666666", LocalDate.of(1985, 6, 15), 4);
        service1 = new Service(1, "Haircut", 50.0, "Basic haircut", 30, Set.of(master1, master2));
        service2 = new Service(2, "Coloring", 100.0, "Hair coloring", 60, Set.of(master1, master2));
        receptionist = new Receptionist("Anna", "Brown", "777888999", LocalDate.of(1992, 7, 10), WorkType.FULL_TIME);

        ServiceInfo serviceInfo = new ServiceInfo(service1, LocalDateTime.now(), Set.of(master1));
        appointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo))
                .receptionist(receptionist)
                .build();
        providedService = appointment.getProvidedServices().iterator().next();
    }

    @Test
    void constructorAddsAssociationsBothSides() {
        assertTrue(service1.getProvidedServices().contains(providedService));
        assertEquals(service1, providedService.getService());
    }

    @Test
    void constructorWithNullServiceThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new ProvidedService.Builder(LocalDateTime.now(), null, appointment, Set.of(master1)).build());
    }

    @Test
    void addProvidedServiceUpdatesBothSides() {
        service2.addProvidedService(providedService);

        assertTrue(service2.getProvidedServices().contains(providedService));
    }

    @Test
    void addNullProvidedServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> service1.addProvidedService(null));
    }

    @Test
    void addDuplicateProvidedServiceDoesNotDuplicate() {
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
    void removeNullServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> providedService.removeService(null));
    }

    @Test
    void removeServiceNotLinkedDoesNothing() {
        providedService.removeService(service2);

        assertEquals(service1, providedService.getService());
    }

    @Test
    void removeNullProvidedServiceThrowsException() {
        assertThrows(NullPointerException.class, () -> service1.removeProvidedService(null));
    }

    @Test
    void removeProvidedServiceNotInSetDoesNothing() {
        int sizeBefore = service2.getProvidedServices().size();

        ProvidedService psFromService1 = appointment.getProvidedServices().iterator().next();
        service2.removeProvidedService(psFromService1);

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
    void getProvidedServicesReturnsCopy() {
        Set<ProvidedService> services = service1.getProvidedServices();
        services.clear();

        assertFalse(service1.getProvidedServices().isEmpty());
    }
}
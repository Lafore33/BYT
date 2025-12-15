package com.example.byt.associations;

import com.example.byt.models.Certification;
import com.example.byt.models.HistoryOfStatus;
import com.example.byt.models.ProvidedService;
import com.example.byt.models.ServiceInfo;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.appointment.PaymentMethod;
import com.example.byt.models.person.*;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentReceptionistAssociationTest {

    Appointment appointment;
    Receptionist receptionist;
    Customer customer;
    Service service;
    Master master;
    Set<ServiceInfo> serviceInfos;

    @BeforeEach
    void setUp() {
        Appointment.clearExtent();
        Receptionist.clearExtent();
        Customer.clearExtent();
        Service.clearExtent();
        Master.clearExtent();
        Certification.clearExtent();
        HistoryOfStatus.clearExtent();
        ProvidedService.clearExtent();

        master = Worker.createMaster("Mike", "Smith", "444555666", LocalDate.of(1985, 3, 20), 5).getMaster();
        service = new Service(1, "Haircut", 50.0, "Basic haircut", 30.0, Set.of(master));
        customer = Person.createCustomer("John", "Doe", "111222333", "john@example.com", LocalDate.of(1990, 5, 15));
        receptionist = Worker.createReceptionist("Anna", "Smith", "12345", LocalDate.of(1990, 1, 1), WorkType.FULL_TIME).getReceptionist();

        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now(), Set.of(master));
        serviceInfos = Set.of(serviceInfo);
        appointment = new Appointment.Builder(LocalDate.now(), customer, serviceInfos).paymentMethod(PaymentMethod.CASH).receptionist(receptionist).build();
    }

    @Test
    void testAddReceptionistSetsReverseReference() {
        Receptionist newReceptionist = Worker.createReceptionist("Jane", "Doe", "99999", LocalDate.of(1992, 2, 2), WorkType.FULL_TIME).getReceptionist();
        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now().plusHours(1), Set.of(master));
        Appointment newAppointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo))
                .paymentMethod(PaymentMethod.CASH)
                .build();
        newAppointment.addReceptionist(newReceptionist);
        assertEquals(newReceptionist, newAppointment.getReceptionist());
        assertTrue(newReceptionist.getAppointments().contains(newAppointment));
    }

    @Test
    void testReceptionistAddAppointmentSetsReverseReference() {
        Receptionist newReceptionist = Worker.createReceptionist("Jane", "Doe", "99999", LocalDate.of(1992, 2, 2), WorkType.FULL_TIME).getReceptionist();
        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now().plusHours(1), Set.of(master));
        Appointment newAppointment = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo))
                .paymentMethod(PaymentMethod.CASH)
                .build();
        newReceptionist.addAppointment(newAppointment);
        assertEquals(newReceptionist, newAppointment.getReceptionist());
        assertTrue(newReceptionist.getAppointments().contains(newAppointment));
    }

    @Test
    void testCannotAssignTwoDifferentReceptionists() {
        Receptionist second = Worker.createReceptionist("Misha", "Koutun", "99999", LocalDate.of(2006, 4, 15), WorkType.PART_TIME).getReceptionist();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointment.addReceptionist(second)
        );
        assertEquals("The appointment is already done by another receptionist", ex.getMessage());
        assertEquals(receptionist, appointment.getReceptionist());
    }

    @Test
    void testAddingSameReceptionistTwiceDoesNothing() {
        appointment.addReceptionist(receptionist);
        assertEquals(1, receptionist.getAppointments().size());
        assertEquals(receptionist, appointment.getReceptionist());
    }

    @Test
    void testNullAppointmentCannotBeAddedToReceptionist() {
        assertThrows(NullPointerException.class, () -> receptionist.addAppointment(null));
    }

    @Test
    void testBuilderInitialReceptionistIsLinkedCorrectly() {
        ServiceInfo serviceInfo = new ServiceInfo(service, LocalDateTime.now().plusHours(2), Set.of(master));
        Appointment a = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo))
                .receptionist(receptionist)
                .build();
        assertEquals(receptionist, a.getReceptionist());
        assertTrue(receptionist.getAppointments().contains(a));
    }

    @Test
    void testReceptionistAppointmentsAreReturnedAsCopy() {
        ServiceInfo serviceInfo2 = new ServiceInfo(service, LocalDateTime.now().plusHours(3), Set.of(master));
        Appointment appointment2 = new Appointment.Builder(LocalDate.now(), customer, Set.of(serviceInfo2))
                .paymentMethod(PaymentMethod.CASH)
                .receptionist(receptionist)
                .build();
        HashSet<Appointment> appointments = receptionist.getAppointments();
        appointments.clear();
        Assertions.assertNotEquals(receptionist.getAppointments(), appointments);
    }
}
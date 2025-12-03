package com.example.byt.associations;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.appointment.PaymentMethod;
import com.example.byt.models.person.Receptionist;
import com.example.byt.models.person.WorkType;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentReceptionistAssociationTest {

    Appointment appointment;
    Receptionist receptionist;

    @BeforeEach
    void setUp() {
        Appointment.clearExtent();
        Receptionist.clearExtent();

        receptionist = new Receptionist(
                "Anna", "Smith", "12345",
                LocalDate.of(1990, 1, 1),
                WorkType.FULL_TIME
        );

        appointment = new Appointment.Builder(LocalDate.now())
                .paymentMethod(PaymentMethod.CASH)
                .build();
    }

    @Test
    void testAddReceptionistSetsReverseReference() {
        appointment.addReceptionist(receptionist);

        assertEquals(receptionist, appointment.getReceptionist());
        assertTrue(receptionist.getAppointments().contains(appointment));
    }

    @Test
    void testReceptionistAddAppointmentSetsReverseReference() {
        receptionist.addAppointment(appointment);

        assertEquals(receptionist, appointment.getReceptionist());
        assertTrue(receptionist.getAppointments().contains(appointment));
    }

    @Test
    void testCannotAssignTwoDifferentReceptionists() {
        Receptionist second = new Receptionist(
                "Misha", "Koutun", "99999",
                LocalDate.of(2006, 4, 15),
                WorkType.PART_TIME
        );

        appointment.addReceptionist(receptionist);

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
        Appointment a = new Appointment.Builder(LocalDate.now())
                .receptionist(receptionist)
                .build();

        assertEquals(receptionist, a.getReceptionist());
        assertTrue(receptionist.getAppointments().contains(a));
    }

    @Test
    void testReceptionistAppointmentsAreReturnedAsCopy() {
        Appointment appointment2 = new Appointment.Builder(LocalDate.now())
                .paymentMethod(PaymentMethod.CASH)
                .build();

        appointment.addReceptionist(receptionist);
        appointment2.addReceptionist(receptionist);

        HashSet<Appointment> appointments = receptionist.getAppointments();
        appointments.clear();

        Assertions.assertNotEquals(receptionist.getAppointments(), appointments);
    }
}

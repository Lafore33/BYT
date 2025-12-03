package com.example.byt.models.person;

import com.example.byt.models.appointment.Appointment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Receptionist extends Worker {

    @NotNull
    private WorkType workType;

    private List<Appointment> appointments = new ArrayList<>();

    private static List<Receptionist> receptionists = new ArrayList<>();

    public Receptionist(String name, String surname, String phoneNumber, LocalDate birthDate, WorkType workType) {
        super(name, surname, phoneNumber, birthDate);
        this.workType = workType;
        addReceptionist(this);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        if (this.appointments.contains(appointment)) {
            return;
        }
        if (appointment == null) {
            throw new NullPointerException("Null appointment cannot be added to receptionist");
        }
        appointments.add(appointment);
        appointment.addReceptionist(this);
    }


    private static void addReceptionist(Receptionist receptionist){
        if (receptionist == null){
            throw new NullPointerException("Receptionist cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the receptionist cannot be added to the list");
            return;
        }
        receptionists.add(receptionist);
    }

    public WorkType getWorkType() {
        return workType;
    }

    public static List<Receptionist> getReceptionistList() {
        return new ArrayList<>(receptionists);
    }

    public static void clearExtent() {
        receptionists.clear();
    }
}

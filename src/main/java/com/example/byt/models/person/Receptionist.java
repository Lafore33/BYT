package com.example.byt.models.person;

import com.example.byt.models.appointment.Appointment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Receptionist implements Serializable {

    @NotNull
    private WorkType workType;

    private HashSet<Appointment> appointments = new HashSet<>();

    private static List<Receptionist> receptionists = new ArrayList<>();
    private Worker worker;
    private Receptionist() {
    }

    protected Receptionist(Worker worker, WorkType workType) {
        if (worker == null) {
            throw new NullPointerException("Worker cannot be null");
        }
        this.worker = worker;
        this.workType = workType;
        addReceptionist(this);
    }

    public HashSet<Appointment> getAppointments() {
        return new HashSet<>(appointments);
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

    public Worker getWorker() {
        return worker;
    }
    public void removeAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new NullPointerException("Appointment cannot be null");
        }
        if (!appointments.contains(appointment)) {
            return;
        }
        appointments.remove(appointment);
        appointment.removeReceptionist(this);
    }

    public void removeReceptionist() {
        for (Appointment appointment : new HashSet<>(appointments)) {
            removeAppointment(appointment);
        }
        receptionists.remove(this);
    }


}

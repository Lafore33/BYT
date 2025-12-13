package com.example.byt.models.person;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Receptionist implements Serializable {

    private Worker worker;

    @NotNull
    private WorkType workType;

    private HashSet<Appointment> appointments = new HashSet<>();

    private static List<Receptionist> receptionists = new ArrayList<>();

    private Receptionist() {
    }

    public Receptionist(Worker worker, WorkType workType) {
        if (worker == null) {
            throw new NullPointerException("Worker cannot be null");
        }
        if (worker.isReceptionist()) {
            throw new IllegalStateException("Worker is already a Receptionist");
        }
        if (worker.isMaster()) {
            throw new IllegalStateException("Worker is already a Master. Use changeToReceptionist() first.");
        }
        this.worker = worker;
        this.workType = workType;
        worker.setReceptionistRole(this);
        addReceptionist(this);
    }

    public Receptionist(String name, String surname, String phoneNumber, LocalDate birthDate, WorkType workType) {
        this(new Worker(name, surname, phoneNumber, birthDate), workType);
    }
    public Master changeToMaster(int experience) {
        Worker w = this.worker;
        removeReceptionist();
        return new Master(w, experience);
    }

    public Master changeToMaster(int experience, Set<Service> servicesSpecialisesIn) {
        Worker worker = this.worker;
        removeReceptionist();
        return new Master(worker, experience, servicesSpecialisesIn);
    }

    public Worker getWorker() {
        return worker;
    }

    public String getName() {
        return worker.getName();
    }

    public String getSurname() {
        return worker.getSurname();
    }

    public String getPhoneNumber() {
        return worker.getPhoneNumber();
    }

    public LocalDate getBirthDate() {
        return worker.getBirthDate();
    }

    public Person getPerson() {
        return worker.getPerson();
    }

    public HashSet<Appointment> getAppointments() {
        return new HashSet<>(appointments);
    }

    public void addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new NullPointerException("Null appointment cannot be added to receptionist");
        }
        if (this.appointments.contains(appointment)) {
            return;
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

    public void removeReceptionist() {
        for (Appointment appointment : new HashSet<>(appointments)) {
            appointments.remove(appointment);
        }
        if (worker != null) {
            worker.clearReceptionistRole();
        }
        receptionists.remove(this);
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
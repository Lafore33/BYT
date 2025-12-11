package com.example.byt.models;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HistoryOfStatus implements Serializable {

    @NotNull
    private AppointmentStatus status;

    private Customer customer;

    private Appointment appointment;

    @NotNull
    @PastOrPresent
    private LocalDate dateOfChangingStatus;

    private static List<HistoryOfStatus> historyOfStatuses = new ArrayList<>();

    private HistoryOfStatus() {
    }

    public HistoryOfStatus(AppointmentStatus status, LocalDate dateOfChangingStatus, Customer customer, Appointment appointment) {
        this.status = status;
        this.dateOfChangingStatus = dateOfChangingStatus;

        addCustomer(customer);
        addAppointment(appointment);

        addHistoryOfStatus(this);
    }

    private static void addHistoryOfStatus(HistoryOfStatus historyOfStatus){
        if (historyOfStatus == null){
            throw new NullPointerException("HistoryOfStatus cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(historyOfStatus);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the history cannot be added to the list");
            return;
        }
        historyOfStatuses.add(historyOfStatus);
    }

    public void addCustomer(Customer customer){
        if  (customer == null){
            throw new NullPointerException("Customer cannot be null");
        }

        if (this.customer == customer){
            return;
        }

        this.customer = customer;
        customer.addHistory(this);
    }

    public void addAppointment(Appointment appointment){
        if (appointment == null){
            throw new NullPointerException("Appointment cannot be null");
        }

        if (this.appointment == appointment){
            return;
        }

        this.appointment = appointment;
        appointment.addHistory(this);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public LocalDate getDateOfChangingStatus() {
        return dateOfChangingStatus;
    }

    public static List<HistoryOfStatus> getHistoryOfStatusList() {
        return new ArrayList<>(historyOfStatuses);
    }

    public static void clearExtent() {
        historyOfStatuses.clear();
    }

}

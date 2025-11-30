package com.example.byt.models.appointment;

import com.example.byt.models.ProvidedService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Appointment {
    @NotNull
    private LocalDate date;

    private List<String> notes;

    private PaymentMethod paymentMethod;

    @Min(0)
    private double totalPrice;
    private Set<ProvidedService> providedServices = new HashSet<>();

    private static List<Appointment> appointments = new ArrayList<>();

    public Appointment(Builder builder) {
        this.date = builder.date;
        this.notes = builder.notes;
        this.paymentMethod = builder.paymentMethod;
        addAppointment(this);
    }

    private static void addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new NullPointerException("Appointment cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the appointment cannot be added to the list");
            return;
        }
        appointments.add(appointment);
    }
    public void addProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        if (providedServices.contains(providedService)) {
            throw new IllegalArgumentException("ProvidedService is already assigned to this Appointment");
        }
        Appointment oldAppointment = providedService.getAppointment();
        if (oldAppointment != null && oldAppointment != this) {
            oldAppointment.removeProvidedServiceInternal(providedService);
        }

        providedServices.add(providedService);
        if (providedService.getAppointment() != this) {
            providedService.setAppointmentInternal(this);
        }
    }

    public void addProvidedServiceInternal(ProvidedService providedService) {
        if (!providedServices.contains(providedService)) {
            providedServices.add(providedService);
        }
    }
    public void removeProvidedServiceInternal(ProvidedService providedService) {
        providedServices.remove(providedService);
    }
    public Set<ProvidedService> getProvidedServices() {
        return new HashSet<>(providedServices);
    }
    public List<String> getNotes() {
        return notes == null ? null : new ArrayList<>(notes);
    }

    public void setNotes(List<String> notes) {
        if (notes == null) {
            this.notes = null;
            return;
        }
        if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
            throw new IllegalArgumentException("notes can't contain null or empty elements");
        }
        this.notes = new ArrayList<>(notes);
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static class Builder {
        @NotNull
        private LocalDate date;

        private List<String> notes;

        private PaymentMethod paymentMethod;

        public Builder(LocalDate date) {
            this.date = date;
        }

        public Builder notes(List<String> notes) {
            if (notes == null) {
                this.notes = null;
                return this;
            }
            if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
                throw new IllegalArgumentException("notes can't contain null or empty elements");
            }
            this.notes = new ArrayList<>(notes);
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Appointment build(){
            return new Appointment(this);
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getDate() {
        return date;
    }
    public double getTotalPrice() {
        recalculateTotalPrice();
        return totalPrice;
    }

    private void recalculateTotalPrice() {
        double total = 0;
        for (ProvidedService ps : providedServices) {
            total += ps.getPrice();
        }
        this.totalPrice = total;
    }

    public static List<Appointment> getAppointmentList() {
        return new ArrayList<>(appointments);
    }

    public static void clearExtent() {
        appointments.clear();
    }
}
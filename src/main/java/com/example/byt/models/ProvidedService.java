package com.example.byt.models;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProvidedService {

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    @NotNull
    private LocalDateTime time;

    @Min(0)
    private double price;

    private Service service;

    private Appointment appointment;

    private static List<ProvidedService> providedServices = new ArrayList<>();

    private ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;

        addService(builder.service);
        addAppointment(builder.appointment);

        addProvidedService(this);
    }

    private static void addProvidedService(ProvidedService providedService){
        if (providedService == null) {
            throw new NullPointerException("ProvidedService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the provided service cannot be added to the list");
            return;
        }
        providedServices.add(providedService);
    }

    public void addService(Service service){
        if (service == null) {
            throw new NullPointerException("Service cannot be null");
        }

        if (this.service == service) {
            return;
        }

        this.service = service;
        service.addProvidedService(this);
    }

    public void addAppointment(Appointment appointment){
        if (appointment == null) {
            throw new NullPointerException("Appointment cannot be null");
        }

        if (this.appointment == appointment) {
            return;
        }

        this.appointment = appointment;
        appointment.addProvidedService(this);
    }

    public void removeAppointment(Appointment appointment){
        if (appointment == null) {
            throw new NullPointerException("Appointment cannot be null");
        }

        if (this.appointment != appointment) {
            return;
        }

        this.appointment = null;
        appointment.removeProvidedService(this);
        removeService(service);
    }

    public void removeService(Service service){
        if (service == null) {
            throw new NullPointerException("Service cannot be null");
        }

        if (this.service != service) {
            return;
        }

        this.service = null;
        appointment.removeProvidedService(this);
        service.removeProvidedService(this);
    }

    public Service getService() {
        return service;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        if (comment == null) {
            this.comment = null;
        } else if (comment.trim().isEmpty()) {
            this.comment = null;
        } else {
            this.comment = comment;
        }
    }


    public static class Builder {
        @Min(1)
        @Max(5)
        private Integer rating;

        private Service service;

        private Appointment appointment;

        private String comment;

        @NotNull
        private LocalDateTime time;

        public Builder(LocalDateTime time, Service service, Appointment appointment) {
            this.time = time;
            this.service = service;
            this.appointment = appointment;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            if (comment == null) {
                this.comment = null;
            } else if (comment.trim().isEmpty()) {
                this.comment = null;
            } else {
                this.comment = comment;
            }
            return this;
        }

        public ProvidedService build() {
            return new ProvidedService(this);
        }
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public static List<ProvidedService> getProvidedServiceList() {
        return new ArrayList<>(providedServices);
    }

    public static void clearExtent() {
        providedServices.clear();
    }
}

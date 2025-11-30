package com.example.byt.models;

import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    private Set<Master> masters = new HashSet<>();
    private static final int MIN_MASTERS = 1;
    private static final int MAX_MASTERS = 2;
    private Service service;
    private Appointment appointment;

    private static List<ProvidedService> providedServices = new ArrayList<>();
    private ProvidedService(LocalDateTime time, Integer rating, String comment,
                            Set<Master> masters, Service service, Appointment appointment) {
        this.time = time;
        this.rating = rating;
        this.comment = comment;
        if (masters != null && !masters.isEmpty()) {
            for (Master master : masters) {
                addMasterWithoutReverse(master);
                master.addProvidedServiceInternal(this);
            }
        }
        if (service != null) {
            this.service = service;
            service.addProvidedServiceInternal(this);
        }
        if (appointment != null) {
            this.appointment = appointment;
            appointment.addProvidedServiceInternal(this);
        }
        addProvidedService(this);
    }

    private void addMasterWithoutReverse(Master master) {
        if (master == null) {
            throw new IllegalArgumentException("Master cannot be null");
        }
        if (masters.size() >= MAX_MASTERS) {
            throw new IllegalStateException(
                    "Cannot add more than " + MAX_MASTERS + " masters to a ProvidedService"
            );
        }
        masters.add(master);
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
    public void addMaster(Master master) {
        if (master == null) {
            throw new IllegalArgumentException("Master cannot be null");
        }

        if (masters.contains(master)) {
            throw new IllegalArgumentException("Master is already assigned to this ProvidedService");
        }

        if (masters.size() >= MAX_MASTERS) {
            throw new IllegalStateException(
                    "Cannot add more than " + MAX_MASTERS + " masters to a ProvidedService"
            );
        }
        masters.add(master);
        if (!master.getProvidedServices().contains(this)) {
            master.addProvidedServiceInternal(this);
        }
    }

    public void addMasterInternal(Master master) {
        if (!masters.contains(master)) {
            if (masters.size() >= MAX_MASTERS) {
                throw new IllegalStateException(
                        "Cannot add more than " + MAX_MASTERS + " masters to a ProvidedService"
                );
            }
            masters.add(master);
        }
    }

    public void removeMaster(Master master) {
        if (master == null) {
            throw new IllegalArgumentException("Master cannot be null");
        }
        if (!masters.contains(master)) {
            throw new IllegalArgumentException("Master is not assigned to this ProvidedService");
        }
        if (masters.size() <= MIN_MASTERS) {
            throw new IllegalStateException(
                    "Cannot remove master. ProvidedService must have at least " + MIN_MASTERS + " master(s)"
            );
        }
        masters.remove(master);
        if (master.getProvidedServices().contains(this)) {
            master.removeProvidedServiceInternal(this);
        }
    }

    public void removeMasterInternal(Master master) {
        masters.remove(master);
    }
    public Set<Master> getMasters() {
        return new HashSet<>(masters);
    }

    public void setService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null - multiplicity is 1");
        }
        if (this.service == service) {
            return;
        }
        Service oldService = this.service;
        this.service = service;
        if (oldService != null && oldService.getProvidedServices().contains(this)) {
            oldService.removeProvidedServiceInternal(this);
        }
        if (!service.getProvidedServices().contains(this)) {
            service.addProvidedServiceInternal(this);
        }
    }

    public void setServiceInternal(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public void setAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null - multiplicity is 1");
        }
        if (this.appointment == appointment) {
            return;
        }
        Appointment oldAppointment = this.appointment;
        this.appointment = appointment;
        if (oldAppointment != null && oldAppointment.getProvidedServices().contains(this)) {
            oldAppointment.removeProvidedServiceInternal(this);
        }
        if (!appointment.getProvidedServices().contains(this)) {
            appointment.addProvidedServiceInternal(this);
        }
    }

    public void setAppointmentInternal(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void validateAssociations() {
        if (masters.isEmpty()) {
            throw new IllegalStateException(
                    "ProvidedService must have at least " + MIN_MASTERS + " master(s)"
            );
        }
        if (service == null) {
            throw new IllegalStateException("ProvidedService must have a Service assigned");
        }
        if (appointment == null) {
            throw new IllegalStateException("ProvidedService must have an Appointment assigned");
        }
    }

    public static class Builder {
        @Min(1)
        @Max(5)
        private Integer rating;

        private String comment;

        @NotNull
        private LocalDateTime time;

        private Set<Master> masters = new HashSet<>();
        private Service service;
        private Appointment appointment;

        public Builder(LocalDateTime time){
            this.time = time;
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

        public Builder addMaster(Master master) {
            if (master == null) {
                throw new IllegalArgumentException("Master cannot be null");
            }
            if (masters.size() >= MAX_MASTERS) {
                throw new IllegalStateException(
                        "Cannot add more than " + MAX_MASTERS + " masters"
                );
            }
            this.masters.add(master);
            return this;
        }
        public Builder service(Service service) {
            if (service == null) {
                throw new IllegalArgumentException("Service cannot be null");
            }
            this.service = service;
            return this;
        }
        public Builder appointment(Appointment appointment) {
            if (appointment == null) {
                throw new IllegalArgumentException("Appointment cannot be null");
            }
            this.appointment = appointment;
            return this;
        }

        public ProvidedService build() {
            if (masters.isEmpty()) {
                throw new IllegalStateException(
                        "ProvidedService must have at least " + MIN_MASTERS + " master(s)"
                );
            }
            if (service == null) {
                throw new IllegalStateException("Service must be set before building ProvidedService");
            }
            if (appointment == null) {
                throw new IllegalStateException("Appointment must be set before building ProvidedService");
            }

            return new ProvidedService(time, rating, comment, masters, service, appointment);
        }
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
    public double getPrice() {
        recalculatePrice();
        return price;
    }

    private void recalculatePrice() {
        if (service == null || masters.isEmpty()) {
            this.price = 0.0;
            return;
        }

        double servicePrice = service.getRegularPrice();
        double totalExperience = 0;
        for (Master master : masters) {
            totalExperience += master.getExperience();
        }

        this.price = servicePrice + totalExperience;
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
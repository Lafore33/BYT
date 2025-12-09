package com.example.byt.models;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Master;
import com.example.byt.models.services.FourHandsService;
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

    private Service service;

    private Appointment appointment;

    private Set<Master> completedByMasters = new HashSet<>();

    private static final int MIN_MASTERS = 1;
    private static final int MAX_MASTERS = 2;

    private static List<ProvidedService> providedServices = new ArrayList<>();

    private ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;
        addService(builder.service);
        addAppointment(builder.appointment);
        if (builder.masters == null || builder.masters.isEmpty()) {
            throw new IllegalArgumentException("ProvidedService must have at least " + MIN_MASTERS + " master(s)");
        }
        for (Master master : builder.masters) {
            addMaster(master);
        }
        validateMasterCount();
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

    public void addMaster(Master master) {
        if (master == null) {
            throw new NullPointerException("Master cannot be null");
        }
        if (completedByMasters.contains(master)) {
            return;
        }
        if (completedByMasters.size() >= MAX_MASTERS) {
            throw new IllegalStateException(
                    "Cannot add more masters. Maximum is " + MAX_MASTERS
            );
        }
        validateMasterSpecialization(master);
        completedByMasters.add(master);
        master.addCompletedService(this);
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

    public void removeMaster(Master master) {
        if (master == null) {
            throw new NullPointerException("Master cannot be null");
        }
        if (!completedByMasters.contains(master)) {
            return;
        }
        if (completedByMasters.size() <= MIN_MASTERS) {
            throw new IllegalStateException(
                    "Cannot remove master. ProvidedService must have at least " + MIN_MASTERS + " master(s)"
            );
        }
        completedByMasters.remove(master);
        master.removeCompletedService(this);
    }

    private void validateMasterCount() {
        if (service == null) {
            return;
        }
        int required = getRequiredMasterCount();
        if (completedByMasters.size() < required) {
            throw new IllegalArgumentException(
                    "This service requires " + required + " master(s), but only " +
                            completedByMasters.size() + " provided"
            );
        }
        if (completedByMasters.size() > MAX_MASTERS) {
            throw new IllegalArgumentException(
                    "ProvidedService cannot have more than " + MAX_MASTERS + " masters"
            );
        }
    }

    private int getRequiredMasterCount() {
        if (service instanceof FourHandsService) {
            return FourHandsService.getNumOfSpecialistsRequired();
        }
        return MIN_MASTERS;
    }

    private void validateMasterSpecialization(Master master) {
        if (service == null) {
            return;
        }
        if (!master.getServiceSpecialisesIn().contains(service)) {
            throw new IllegalArgumentException(
                    "Master '" + master.getName() + " " + master.getSurname() +
                            "' does not specialize in service '" + service.getName() + "'. " +
                            "Only masters who specialize in the service can complete it."
            );
        }
    }

    public Service getService() {
        return service;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public Set<Master> getCompletedByMasters() {
        return new HashSet<>(completedByMasters);
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

    public double getPrice() {
        if (service == null) {
            return 0.0;
        }
        double basePrice = service.getTotalPrice();
        boolean hasTopMaster = completedByMasters.stream().anyMatch(Master::isTopMaster);
        if (hasTopMaster) {
            return basePrice * 1.2;
        }
        return basePrice;
    }


    public static class Builder {
        @Min(1)
        @Max(5)
        private Integer rating;

        private Service service;

        private Appointment appointment;

        private Set<Master> masters;

        private String comment;

        @NotNull
        private LocalDateTime time;

        public Builder(LocalDateTime time, Service service, Appointment appointment, Set<Master> masters) {
            if (masters == null || masters.isEmpty()) {
                throw new IllegalArgumentException("At least one master is required");
            }
            this.time = time;
            this.service = service;
            this.appointment = appointment;
            this.masters = new HashSet<>(masters);
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

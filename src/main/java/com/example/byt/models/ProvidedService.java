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

    private Appointment doneDuring;
    private Service refersTo;
    private Set<Master> completedBy = new HashSet<>();

    private static final int MIN_MASTERS = 1;
    private static final int MAX_MASTERS = 2;

    private static List<ProvidedService> providedServices = new ArrayList<>();

    private ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;
        addProvidedService(this);
    }

    private ProvidedService(Appointment appointment, Service service, Set<Master> masters, LocalDateTime time, Integer rating, String comment) {
        if (appointment == null) {
            throw new IllegalArgumentException("ProvidedService must be associated with an Appointment");
        }
        if (service == null) {
            throw new IllegalArgumentException("ProvidedService must be associated with a Service");
        }
        if (masters == null || masters.isEmpty()) {
            throw new IllegalArgumentException("ProvidedService must have at least " + MIN_MASTERS + " Master (1..2)");
        }
        if (masters.size() > MAX_MASTERS) {
            throw new IllegalArgumentException("ProvidedService cannot have more than " + MAX_MASTERS + " Masters (1..2)");
        }

        this.doneDuring = appointment;
        this.refersTo = service;
        this.time = time;
        this.rating = rating;
        setComment(comment);

        service.addProvidedAs(this);

        for (Master master : masters) {
            if (master == null) {
                throw new IllegalArgumentException("Master cannot be null");
            }
            if (completedBy.add(master)) {
                master.addServiceCompleted(this);
            }
        }

        addProvidedService(this);
    }

    public static ProvidedService createProvidedService(Appointment appointment, Service service, Set<Master> masters, LocalDateTime time) {
        return new ProvidedService(appointment, service, masters, time, null, null);
    }

    private static void addProvidedService(ProvidedService providedService) {
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

    public void removeProvidedService() {
        if (doneDuring != null && doneDuring.getServicesDone().size() <= 1) {
            throw new IllegalStateException("Cannot remove ProvidedService: Appointment must have at least one service");
        }

        if (refersTo != null) {
            refersTo.removeProvidedAs(this);
        }

        for (Master master : new HashSet<>(completedBy)) {
            if (completedBy.remove(master)) {
                master.removeServiceCompleted(this);
            }
        }

        completedBy.clear();
        providedServices.remove(this);
    }

    public void addMasterCompletedBy(Master master) {
        if (master == null) {
            throw new IllegalArgumentException("Master cannot be null");
        }
        if (completedBy.size() >= MAX_MASTERS) {
            throw new IllegalStateException("ProvidedService cannot have more than " + MAX_MASTERS + " Masters (1..2). Current: " + completedBy.size());
        }
        if (completedBy.add(master)) {
            master.addServiceCompleted(this);
        }
    }

    public void removeMasterCompletedBy(Master master) {
        if (master == null) return;

        if (completedBy.size() <= MIN_MASTERS) {
            throw new IllegalStateException("ProvidedService must have at least " + MIN_MASTERS + " Master (1..2). Cannot remove the last one.");
        }

        if (completedBy.remove(master)) {
            master.removeServiceCompleted(this);
        }
    }

    public static class Builder {
        @Min(1)
        @Max(5)
        private Integer rating;

        private String comment;

        @NotNull
        private LocalDateTime time;

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

        public ProvidedService build() {
            return new ProvidedService(this);
        }
    }

    public Appointment getDoneDuring() {
        return doneDuring;
    }

    public Service getRefersTo() {
        return refersTo;
    }

    public Set<Master> getCompletedBy() {
        return new HashSet<>(completedBy);
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

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public static List<ProvidedService> getProvidedServiceList() {
        return new ArrayList<>(providedServices);
    }

    public static void clearExtent() {
        providedServices.clear();
    }
}
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

    private Appointment appointmentDoneDuring;
    private Service serviceRefersTo;
    private Set<Master> completedBy = new HashSet<>();

    private static final int MIN_MASTERS = 1;
    private static final int MAX_MASTERS = 2;
    private static final double TOP_MASTER_SURCHARGE = 0.20;

    private static List<ProvidedService> providedServices = new ArrayList<>();

    private ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;
        this.appointmentDoneDuring = builder.appointment;
        this.serviceRefersTo = builder.service;

        if (builder.appointment != null) {
            builder.appointment.addProvidedServiceInternal(this);
        }

        if (builder.service != null) {
            builder.service.addProvidedAs(this);
        }

        for (Master master : builder.masters) {
            completedBy.add(master);
            master.addServiceCompleted(this);
        }

        if (builder.service != null) {
            this.price = calculatePrice(builder.service, builder.masters);
        }

        addProvidedService(this);
    }

    private double calculatePrice(Service service, Set<Master> masters) {
        double basePriceWithPromotions = service.getTotalPrice();
        boolean hasTopMaster = masters.stream()
                .anyMatch(master -> master.getExperience() >= Master.getMinExperienceForTop());
        if (hasTopMaster) {
            return basePriceWithPromotions * (1 + TOP_MASTER_SURCHARGE);
        }
        return basePriceWithPromotions;
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
        if (appointmentDoneDuring != null && appointmentDoneDuring.getServicesDone().size() <= 1) {
            throw new IllegalStateException("Cannot remove ProvidedService: Appointment must have at least one service");
        }

        if (appointmentDoneDuring != null) {
            appointmentDoneDuring.removeProvidedServiceInternal(this);
        }

        if (serviceRefersTo != null) {
            serviceRefersTo.removeProvidedAs(this);
        }

        for (Master master : new HashSet<>(completedBy)) {
            master.removeServiceCompleted(this);
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

        if (completedBy.contains(master)) {
            return;
        }

        completedBy.add(master);
        master.addServiceCompleted(this);

        if (serviceRefersTo != null) {
            this.price = calculatePrice(serviceRefersTo, completedBy);
        }
    }

    public void removeMasterCompletedBy(Master master) {
        if (master == null) return;

        if (completedBy.size() <= MIN_MASTERS) {
            throw new IllegalStateException("ProvidedService must have at least " + MIN_MASTERS + " Master (1..2). Cannot remove the last one.");
        }

        if (completedBy.remove(master)) {
            master.removeServiceCompleted(this);

            if (serviceRefersTo != null) {
                this.price = calculatePrice(serviceRefersTo, completedBy);
            }
        }
    }

    public static class Builder {
        @NotNull
        private final Appointment appointment;

        @NotNull
        private final Service service;

        @NotNull
        private final Set<Master> masters;

        @NotNull
        private final LocalDateTime time;

        @Min(1)
        @Max(5)
        private Integer rating;

        private String comment;

        public Builder(Appointment appointment, Service service, Set<Master> masters, LocalDateTime time) {
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
            for (Master master : masters) {
                if (master == null) {
                    throw new IllegalArgumentException("Master cannot be null");
                }
            }

            this.appointment = appointment;
            this.service = service;
            this.masters = new HashSet<>(masters);
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

    public Appointment getAppointmentDoneDuring() {
        return appointmentDoneDuring == null ? null : new Appointment(appointmentDoneDuring);
    }

    public Service getServiceRefersTo() {
        return serviceRefersTo == null ? null : new Service(serviceRefersTo);
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

    public static double getTopMasterSurcharge() {
        return TOP_MASTER_SURCHARGE;
    }

    public static List<ProvidedService> getProvidedServiceList() {
        return new ArrayList<>(providedServices);
    }

    public static void clearExtent() {
        providedServices.clear();
    }
}
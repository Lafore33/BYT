package com.example.byt.models.services;

import com.example.byt.models.ProvidedService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service implements Serializable {

    @Min(0)
    private int id;

    @NotBlank
    private String name;

    @Min(0)
    private double regularPrice;

    @NotBlank
    private String description;

    @Min(0)
    private double duration;

    // Derived attribute: based on all ratings from ProvidedServices
    @Min(0)
    @Max(5)
    private double rating;

    @Min(0)
    private double totalPrice;
    private Set<ProvidedService> providedServices = new HashSet<>();

    // added default constructor for proper deserialization
    // made it protected on purpose, so that it is used only by the inheritors
    protected Service() {
    }

    private static List<Service> services = new ArrayList<>();

    public Service(int id, String name, double regularPrice, String description,
                   double duration) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
        addService(this);
    }

    private static void addService(Service service){
        if (service == null){
            throw new NullPointerException("Service cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        services.add(service);
    }
    public void addProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        if (providedServices.contains(providedService)) {
            throw new IllegalArgumentException("ProvidedService is already assigned to this Service");
        }
        Service oldService = providedService.getService();
        if (oldService != null && oldService != this) {
            oldService.removeProvidedServiceInternal(providedService);
        }
        providedServices.add(providedService);
        if (providedService.getService() != this) {
            providedService.setServiceInternal(this);
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

    public static List<Service> getServiceList() {
        return new ArrayList<>(services);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        recalculateRating();
        return rating;
    }

    private void recalculateRating() {
        if (providedServices.isEmpty()) {
            this.rating = 0.0;
            return;
        }
        double sum = 0;
        int count = 0;
        for (ProvidedService ps : providedServices) {
            if (ps.getRating() != null) {
                sum += ps.getRating();
                count++;
            }
        }
        this.rating = count > 0 ? sum / count : 0.0;
    }

    public double getDuration() {
        return duration;
    }

    public static void clearExtent() {
        services.clear();
    }
}
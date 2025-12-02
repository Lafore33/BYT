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

    @Min(0)
    @Max(5)
    private double rating;

    @Min(0)
    private double totalPrice;
    private Set<ProvidedService> providedAs = new HashSet<>();

    // added default constructor for proper deserialization
    // made it protected on purpose, so that it is used only by the inheritors
    protected Service() {
    }

    private static List<Service> services = new ArrayList<>();

    public Service(int id, String name, double regularPrice, String description, double duration) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
        addService(this);
    }
    public Service(Service other) {
        this.id = other.id;
        this.name = other.name;
        this.regularPrice = other.regularPrice;
        this.description = other.description;
        this.duration = other.duration;
        this.rating = other.rating;
        this.totalPrice = other.totalPrice;
        this.providedAs = new HashSet<>(other.providedAs);
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

    public void addProvidedAs(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        providedAs.add(providedService);
    }

    public void removeProvidedAs(ProvidedService providedService) {
        if (providedService != null) {
            providedAs.remove(providedService);
        }
    }

    public Set<ProvidedService> getProvidedAs() {
        return new HashSet<>(providedAs);
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

    public double getDuration() {
        return duration;
    }

    public static void clearExtent() {
        services.clear();
    }
}
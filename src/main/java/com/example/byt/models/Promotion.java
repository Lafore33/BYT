package com.example.byt.models;

import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Promotion implements Serializable {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(5)
    @Max(50)
    private double percentage;

    private LocalDate startDate;

    private LocalDate endDate;

    private Set<Service> servicesApplicableTo = new HashSet<>();

    private static List<Promotion> promotions = new ArrayList<>();

    private Promotion() {
    }

    public Promotion(String name, String description, double percentage, LocalDate startDate, LocalDate endDate, Set<Service> servicesApplicableTo) {
        if(servicesApplicableTo == null || servicesApplicableTo.isEmpty()){
            throw new IllegalArgumentException("Promotion must have at least one service");
        }
        this.name = name;
        this.description = description;
        this.percentage = percentage;
        setStartDate(startDate);
        setEndDate(endDate);
        addPromotion(this);

        for(Service service : servicesApplicableTo){
            addServiceApplicableTo(service);
        }
    }

    private static void addPromotion(Promotion promotion){
        if (promotion == null){
            throw new NullPointerException("Promotion cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Promotion>> violations = validator.validate(promotion);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the promotion cannot be added to the list");
            return;
        }
        promotions.add(promotion);
    }

    public void addServiceApplicableTo(Service service){
        if(service == null)
            throw new IllegalArgumentException("Service cannot be null");
        if(servicesApplicableTo.add(service))
            service.addPromotionApplied(this);
    }

    public void removeServiceApplicableTo(Service service){
        if(service == null) return;
        if(servicesApplicableTo.remove(service)){
            service.removePromotionApplied(this);
        }
        if(servicesApplicableTo.isEmpty()) {
            addServiceApplicableTo(service);
            throw new IllegalStateException("Promotion must have at least one service");
        }
    }

    public Set<Service> getServicesApplicableTo(){
        return new HashSet<>(servicesApplicableTo);
    }


    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must not be after endDate");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("startDate must be set before endDate");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPercentage() {
        return percentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public static List<Promotion> getPromotionList() {
        return new ArrayList<>(promotions);
    }

    public static void clearExtent() {
        promotions.clear();
    }

}

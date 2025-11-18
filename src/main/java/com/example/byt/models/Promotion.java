package com.example.byt.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Promotion {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(5)
    @Max(50)
    private double percentage;

    private LocalDate startDate;

    private LocalDate endDate;

    private static List<Promotion> promotionList = new ArrayList<>();

    public Promotion(String name, String description, double percentage, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.percentage = percentage;
        setStartDate(startDate);
        setEndDate(endDate);
        addPromotion(this);
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
        promotionList.add(promotion);
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
        return new ArrayList<>(promotionList);
    }

}

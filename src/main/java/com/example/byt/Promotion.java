package com.example.byt;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

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

    public Promotion(String name, String description, double percentage, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.percentage = percentage;
        setStartDate(startDate);
        setEndDate(endDate);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}

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

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public Promotion(String name, String description, double percentage, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.percentage = percentage;
        this.startDate = startDate;
        setEndDate(endDate);
    }

    public void setEndDate(LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.startDate = startDate;
    }
}

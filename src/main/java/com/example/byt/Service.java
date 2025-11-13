package com.example.byt;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Service {

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

    public Service(int id, String name, double regularPrice, String description,
                   double duration) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
    }
}

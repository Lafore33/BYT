package com.example.byt;

import jakarta.validation.constraints.NotBlank;

public class SkinService extends Service {

    @NotBlank
    private String purpose;

    public SkinService(int id, String name, double regularPrice, String description,
                       double duration, double rating, double totalPrice, String purpose) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.purpose = purpose;
    }
}
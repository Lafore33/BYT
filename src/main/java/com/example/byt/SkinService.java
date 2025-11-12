package com.example.byt;

import jakarta.validation.constraints.NotBlank;

public class SkinService extends Service {

    @NotBlank
    private String purpose;

    public SkinService(int id, String name, double regularPrice, String description,
                       double duration, String purpose) {
        super(id, name, regularPrice, description, duration);
        this.purpose = purpose;
    }
}
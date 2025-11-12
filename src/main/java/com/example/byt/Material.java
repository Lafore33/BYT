package com.example.byt;

import jakarta.validation.constraints.NotBlank;

public class Material {
    @NotBlank
    private String name;
    @NotBlank
    private String producer;

    public Material(String name, String producer) {
        this.name = name;
        this.producer = producer;
    }
}

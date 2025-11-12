package com.example.byt;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HairService extends Service{
    @NotNull
    private HairServiceType type;

    @NotEmpty
    private List<String> hairTypes;

    public HairService(int id, String name, double regularPrice, String description, double duration, double rating,
                       double totalPrice, HairServiceType type, List<String> hairTypes) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.type = type;
        this.hairTypes = hairTypes;
    }
}

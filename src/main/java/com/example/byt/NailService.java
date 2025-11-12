package com.example.byt;

import jakarta.validation.constraints.NotNull;

public class NailService extends Service {
    @NotNull
    private NailServiceType type;

    private boolean isCareIncluded;

    public NailService(int id, String name, double regularPrice, String description, double duration, double rating,
                       double totalPrice, NailServiceType type, boolean isCareIncluded) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.type = type;
        this.isCareIncluded = isCareIncluded;
    }

}
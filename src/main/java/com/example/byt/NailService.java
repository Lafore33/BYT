package com.example.byt;

import jakarta.validation.constraints.NotNull;

public class NailService extends Service {
    @NotNull
    private NailServiceType type;

    private boolean isCareIncluded;

    public NailService(int id, String name, double regularPrice, String description,
                       double duration, NailServiceType type, boolean isCareIncluded) {
        super(id, name, regularPrice, description, duration);
        this.type = type;
        this.isCareIncluded = isCareIncluded;
    }

}
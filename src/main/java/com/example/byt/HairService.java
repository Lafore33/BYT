package com.example.byt;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HairService extends Service{
    @NotNull
    private HairServiceType type;

    @NotEmpty
    private List<String> hairTypes;

    public HairService(int id, String name, double regularPrice, String description, double duration, HairServiceType type, List<String> hairTypes) {
        super(id, name, regularPrice, description, duration);
        this.type = type;
        this.hairTypes = new ArrayList<>(hairTypes);
    }

    public List<String> getHairTypes() {
        return hairTypes == null ? null : new ArrayList<>(hairTypes);
    }

    public void setHairTypes(List<String> hairTypes) {
        this.hairTypes = hairTypes == null ? null : new ArrayList<>(hairTypes);
    }
}

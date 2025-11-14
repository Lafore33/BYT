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

    private static List<HairService> hairServiceList = new ArrayList<>();

    public HairService(int id, String name, double regularPrice, String description, double duration, HairServiceType type, List<String> hairTypes) {
        super(id, name, regularPrice, description, duration);
        this.type = type;
        setHairTypes(hairTypes);
        addHairService(this);
    }

    private static void addHairService(HairService hairService){
        if (hairService == null){
            throw new NullPointerException("HairService cannot be null");
        }
        hairServiceList.add(hairService);
    }

    public List<String> getHairTypes() {
        return new ArrayList<>(hairTypes);
    }

    public void setHairTypes(List<String> hairTypes) {
        if(hairTypes == null) //TODO: check cases for null lists
            throw new IllegalArgumentException("hairTypes cannot be null");
        if (hairTypes.stream().anyMatch(t -> t == null || t.trim().isBlank())) {
            throw new IllegalArgumentException("hairTypes can't contain null or empty elements");
        }
        this.hairTypes = new ArrayList<>(hairTypes);
    }
}

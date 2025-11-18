package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<HairService>> violations = validator.validate(hairService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
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

    public HairServiceType getType() {
        return type;
    }

    public static List<HairService> getHairServiceList() {
        return new ArrayList<>(hairServiceList);
    }
}

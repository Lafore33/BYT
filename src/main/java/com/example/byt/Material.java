package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Material {
    @NotBlank
    private String name;
    @NotBlank
    private String producer;

    private static List<Material> materialList = new ArrayList<>();

    public Material(String name, String producer) {
        this.name = name;
        this.producer = producer;
        addMaterial(this);
    }

    private static void addMaterial(Material material){
        if (material == null){
            throw new NullPointerException("Material cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Material>> violations = validator.validate(material);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the material cannot be added to the list");
            return;
        }
        materialList.add(material);
    }
    public static List<Material> getMaterialList() {
        return new ArrayList<>(materialList);
    }

}

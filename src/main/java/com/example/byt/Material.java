package com.example.byt;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

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
        materialList.add(material);
    }

}

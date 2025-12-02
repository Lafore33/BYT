package com.example.byt.models;

import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Material {
    @NotBlank
    private String name;
    @NotBlank
    private String producer;

    private Set<Service> servicesUsedIn = new HashSet<>();

    private static List<Material> materials = new ArrayList<>();

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
        materials.add(material);
    }

    public void removeMaterial(){
        for(Service service : servicesUsedIn){
            removeServiceUsedIn(service);
        }
        materials.remove(this);
    }
    public void addServiceUsedIn(Service service){
        if(service == null)
            throw new IllegalArgumentException("Service cannot be null");
        if(servicesUsedIn.add(service))
            service.addMaterialUsed(this);
    }

    public void removeServiceUsedIn(Service service){
        if(service != null && servicesUsedIn.remove(service))
            service.removeMaterialUsed(this);
    }

    public Set<Service> getServicesUsedIn() {
        return new HashSet<>(servicesUsedIn);
    }
    public static List<Material> getMaterialList() {
        return new ArrayList<>(materials);
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public static void clearExtent() {
        materials.clear();
    }
}

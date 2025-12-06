package com.example.byt.models.services;

import com.example.byt.models.Material;
import com.example.byt.models.Promotion;
import com.example.byt.models.person.Master;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service implements Serializable {

    @Min(0)
    private int id;

    @NotBlank
    private String name;

    @Min(0)
    private double regularPrice;

    @NotBlank
    private String description;

    @Min(0)
    private double duration;

    @Min(0)
    @Max(5)
    private double rating;

    private Set<Material> materialsUsed = new HashSet<>();
    private Set<Promotion> promotionsApplied = new HashSet<>();
    private Set<Master> mastersSpecializedIn = new HashSet<>();

    // added default constructor for proper deserialization
    // made it protected on purpose, so that it is used only by the inheritors
    protected Service() {
    }

    private static List<Service> services = new ArrayList<>();

    public Service(int id, String name, double regularPrice, String description,
                   double duration, Set<Master> masters) {
        if(masters == null || masters.isEmpty())
            throw new IllegalArgumentException("Masters cannot be null or empty");

        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
        addService(this);

        for (Master master : masters) {
            addMasterSpecializedIn(master);
        }
    }

    public Service(int id, String name, double regularPrice, String description, double duration, Set<Master> masters,  Set<Material> materialsUsed) {
        this(id, name, regularPrice, description, duration, masters);

        if (materialsUsed != null) {
            for(Material material : materialsUsed) {
                addMaterialUsed(material);
            }
        }
    }

    private static void addService(Service service){
        if (service == null){
            throw new NullPointerException("Service cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Service>> violations = validator.validate(service);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        services.add(service);
    }


    public void addMaterialUsed(Material material){
        if(material == null)
            throw new IllegalArgumentException("Material cannot be null");
        if(materialsUsed.add(material))
            material.addServiceUsedIn(this);
    }

    public void removeMaterialUsed(Material material){
        if(material != null && materialsUsed.remove(material))
            material.removeServiceUsedIn(this);
    }

    public Set<Material> getMaterialsUsed() {
        return new HashSet<>(materialsUsed);
    }

    public void addPromotionApplied(Promotion promotion){
        if(promotion == null)
            throw new IllegalArgumentException("Promotion cannot be null");
        if(promotionsApplied.add(promotion))
            promotion.addServiceApplicableTo(this);
    }

    public void removePromotionApplied(Promotion promotion){
        if(promotion != null && promotionsApplied.remove(promotion))
            promotion.removeServiceApplicableTo(this);
    }

    public Set<Promotion> getPromotionsApplied() {
        return new HashSet<>(promotionsApplied);
    }

    public void addMasterSpecializedIn(Master master){
        if(master == null)
            throw new IllegalArgumentException("Master cannot be null");
        if(mastersSpecializedIn.add(master))
            master.addServiceSpecialisesIn(this);
    }

    public void removeMasterSpecializedIn(Master master){
        if(master == null) return;
        if(mastersSpecializedIn.remove(master))
            master.removeServiceSpecialisesIn(this);
        if(mastersSpecializedIn.isEmpty()) {
            addMasterSpecializedIn(master);
            throw new IllegalStateException("Service must have at least one master specialized in it");
        }
    }

    public void removeMasterSpecializedInForRemoval(Master master){
        if(master == null) return;
        mastersSpecializedIn.remove(master);
        if(mastersSpecializedIn.isEmpty()) {
            addMasterSpecializedIn(master);
            throw new IllegalStateException("Service must have at least one master specialized in it");
        }

    }

    public Set<Master> getMasterSpecializedIn() {
        return new HashSet<>(mastersSpecializedIn);
    }

    public static List<Service> getServiceList() {
        return new ArrayList<>(services);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public String getDescription() {
        return description;
    }

    public double getDuration() {
        return duration;
    }

    public double getTotalPrice(){
        double maxDiscount = 0;
        LocalDate today = LocalDate.now();

        for (Promotion promo : promotionsApplied) {
            if (!today.isBefore(promo.getStartDate()) && !today.isAfter(promo.getEndDate())) {
                maxDiscount = Math.max(maxDiscount, promo.getPercentage());
            }
        }

        return Math.max(regularPrice * (1 - maxDiscount / 100.0), 0);
    }

    public void removeFromExtent(){
        services.remove(this);
    }

    public static void clearExtent() {
        services.clear();
    }
}

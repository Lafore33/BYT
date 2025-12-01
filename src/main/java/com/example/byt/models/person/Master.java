package com.example.byt.models.person;

import com.example.byt.models.ProvidedService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Master extends Worker {

    @Min(0)
    private int experience;

    private final static int minExperienceForTop = 3;

    private Set<ProvidedService> servicesCompleted = new HashSet<>();

    private static List<Master> masters = new ArrayList<>();

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
        addMaster(this);
    }

    private static void addMaster(Master master){
        if (master == null){
            throw new NullPointerException("Master cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the master cannot be added to the list");
            return;
        }
        masters.add(master);
    }

    public void addServiceCompleted(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        servicesCompleted.add(providedService);
    }

    public void removeServiceCompleted(ProvidedService providedService) {
        if (providedService != null) {
            servicesCompleted.remove(providedService);
        }
    }

    public Set<ProvidedService> getServicesCompleted() {
        return new HashSet<>(servicesCompleted);
    }

    public static List<Master> getMasterList() {
        return new ArrayList<>(masters);
    }

    public int getExperience() {
        return experience;
    }

    public static int getMinExperienceForTop(){
        return minExperienceForTop;
    }

    public static void clearExtent() {
        masters.clear();
    }
}
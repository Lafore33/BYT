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
    private Set<ProvidedService> providedServices = new HashSet<>();
    private static final int PS_MIN_MASTERS = 1;

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
    public void addProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        if (providedServices.contains(providedService)) {
            throw new IllegalArgumentException("ProvidedService is already assigned to this Master");
        }
        providedServices.add(providedService);
        if (!providedService.getMasters().contains(this)) {
            providedService.addMasterInternal(this);
        }
    }

    public void addProvidedServiceInternal(ProvidedService providedService) {
        if (!providedServices.contains(providedService)) {
            providedServices.add(providedService);
        }
    }

    public void removeProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        if (!providedServices.contains(providedService)) {
            throw new IllegalArgumentException("ProvidedService is not assigned to this Master");
        }
        if (providedService.getMasters().size() <= PS_MIN_MASTERS) {
            throw new IllegalStateException(
                    "Cannot remove ProvidedService. It must have at least " + PS_MIN_MASTERS + " master(s)"
            );
        }
        providedServices.remove(providedService);
        if (providedService.getMasters().contains(this)) {
            providedService.removeMasterInternal(this);
        }
    }
    public void removeProvidedServiceInternal(ProvidedService providedService) {
        providedServices.remove(providedService);
    }
    public Set<ProvidedService> getProvidedServices() {
        return new HashSet<>(providedServices);
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
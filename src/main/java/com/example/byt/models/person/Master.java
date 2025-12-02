package com.example.byt.models.person;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.*;

public class Master extends Worker {

    @Min(0)
    private int experience;

    private final static int minExperienceForTop = 3;

    private static List<Master> masters = new ArrayList<>();

    private Master manager;
    private Set<Master> trainees;

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
        this.trainees = new HashSet<>();
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

    public boolean isTopMaster() {
        return this.experience >= minExperienceForTop;
    }

    public void setManager(Master manager) {
        if (manager == this) {
            throw new IllegalArgumentException("A master cannot manage themselves");
        }
        if (manager != null && !manager.isTopMaster()) {
            throw new IllegalStateException("Only top masters (experience >= " + minExperienceForTop + ") can manage trainees.");
        }
        if (this.manager != null) {
            throw new IllegalStateException("This master already has a manager. Remove current manager first.");
        }
        this.manager = manager;
        if (manager != null && !manager.trainees.contains(this)) {
            manager.linkTrainee(this);
        }
    }

    public void removeManager() {
        if (this.manager == null) {
            return;
        }
        Master oldManager = this.manager;
        this.manager = null;
        oldManager.unlinkTrainee(this);
    }

    public void addTrainee(Master trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee cannot be null.");
        }
        if (trainee == this) {
            throw new IllegalArgumentException("A master cannot be their own trainee.");
        }
        if (!this.isTopMaster()) {
            throw new IllegalStateException("Only top masters (experience >= " + minExperienceForTop + ") can manage trainees.");
        }
        if (this.trainees.contains(trainee)) {
            return;
        }
        if (trainee.manager != null && trainee.manager != this) {
            throw new IllegalStateException(
                    "This master already has a different manager. Remove current manager first."
            );
        }
        linkTrainee(trainee);
        if (trainee.manager != this) {
            trainee.manager = this;
        }
    }

    public void removeTrainee(Master trainee) {
        if (trainee == null || !this.trainees.contains(trainee)) {
            return;
        }
        unlinkTrainee(trainee);
        if (trainee.manager == this) {
            trainee.manager = null;
        }
    }

    public void changeManager(Master newManager) {
        if (newManager == this) {
            throw new IllegalArgumentException("A master cannot manage themselves.");
        }
        if (newManager != null && !newManager.isTopMaster()) {
            throw new IllegalStateException("Only top masters (experience >= " + minExperienceForTop + ") can manage trainees.");
        }
        if (this.manager != null) {
            removeManager();
        }
        if (newManager != null) {
            setManager(newManager);
        }
    }

    private void linkTrainee(Master trainee) {
        this.trainees.add(trainee);
    }

    private void unlinkTrainee(Master trainee) {
        this.trainees.remove(trainee);
    }

    public Master getManager() {
        return this.manager;
    }

    public Set<Master> getTrainees() {
        return new HashSet<>(this.trainees);
    }

    public boolean hasManager() {
        return this.manager != null;
    }

    public int getTraineeCount() {
        return this.trainees.size();
    }

    public boolean hasTrainees() {
        return !this.trainees.isEmpty();
    }

    public boolean isTrainee(Master master) {
        return this.trainees.contains(master);
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
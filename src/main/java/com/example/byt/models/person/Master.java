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
    private Set<Master> subordinates;

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
        this.subordinates = new HashSet<>();
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

    public void setManager(Master manager) {
        if (manager == this) {
            throw new IllegalArgumentException("A master cannot manage themselves");
        }
        if (this.manager != null) {
            throw new IllegalStateException("This master already has a manager. Remove current manager first.");
        }
        this.manager = manager;
        if (manager != null && !manager.subordinates.contains(this)) {
            manager.linkSubordinate(this);
        }
    }

    public void removeManager() {
        if (this.manager == null) {
            return;
        }
        Master oldManager = this.manager;
        this.manager = null;
        oldManager.unlinkSubordinate(this);
    }

    public void addSubordinate(Master subordinate) {
        if (subordinate == null) {
            throw new IllegalArgumentException("Subordinate cannot be null.");
        }
        if (subordinate == this) {
            throw new IllegalArgumentException("A master cannot be their own subordinate.");
        }
        if (this.subordinates.contains(subordinate)) {
            return;
        }
        if (subordinate.manager != null && subordinate.manager != this) {
            throw new IllegalStateException(
                    "This master already has a different manager. Remove current manager first."
            );
        }
        this.subordinates.add(subordinate);
        if (subordinate.manager != this) {
            subordinate.manager = this;
        }
    }

    public void removeSubordinate(Master subordinate) {
        if (subordinate == null || !this.subordinates.contains(subordinate)) {
            return;
        }
        this.subordinates.remove(subordinate);
        if (subordinate.manager == this) {
            subordinate.manager = null;
        }
    }

    public void changeManager(Master newManager) {
        if (newManager == this) {
            throw new IllegalArgumentException("A master cannot manage themselves.");
        }
        if (this.manager != null) {
            removeManager();
        }
        if (newManager != null) {
            setManager(newManager);
        }
    }

    private void linkSubordinate(Master subordinate) {
        this.subordinates.add(subordinate);
    }

    private void unlinkSubordinate(Master subordinate) {
        this.subordinates.remove(subordinate);
    }

    public Master getManager() {
        return this.manager;
    }

    public Set<Master> getSubordinates() {
        return new HashSet<>(this.subordinates);
    }

    public boolean hasManager() {
        return this.manager != null;
    }

    public int getSubordinateCount() {
        return this.subordinates.size();
    }

    public boolean hasSubordinates() {
        return !this.subordinates.isEmpty();
    }

    public boolean isSubordinate(Master master) {
        return this.subordinates.contains(master);
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
package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Receptionist extends Worker {

    @NotNull
    private WorkType workType;

    private static List<Receptionist> receptionistList = new ArrayList<>();

    public Receptionist(String name, String surname, String phoneNumber, LocalDate birthDate, WorkType workType) {
        super(name, surname, phoneNumber, birthDate);
        this.workType = workType;
        addReceptionist(this);
    }

    private static void addReceptionist(Receptionist receptionist){
        if (receptionist == null){
            throw new NullPointerException("Receptionist cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        if (!violations.isEmpty()) {
            // TODO: fix this
//            throw new IllegalArgumentException("Validation failed for Receptionist");
            return;
        }
        receptionistList.add(receptionist);
    }
}

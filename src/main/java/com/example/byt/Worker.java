package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Worker extends Person {

    private static List<Worker> workerList = new ArrayList<>();

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
        addWorker(this);
    }

    private static void addWorker(Worker worker){
        if (worker == null){
            throw new NullPointerException("Worker cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Worker>> violations = validator.validate(worker);
        if (!violations.isEmpty()) {
            // TODO: fix this
//            throw new IllegalArgumentException("Validation failed for Worker");
            return;
        }
        workerList.add(worker);
    }

}

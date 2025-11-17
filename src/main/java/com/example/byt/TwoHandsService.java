package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TwoHandsService extends Service {
    private final static int numOfSpecialistsRequired = 1;

    private static List<TwoHandsService> twoHandsServiceList = new ArrayList<>();

    public TwoHandsService(int id, String name, double regularPrice, String description,
                           double duration) {
        super(id, name, regularPrice, description, duration);
        addTwoHandsService(this);
    }

    private static void addTwoHandsService(TwoHandsService twoHandsService){
        if (twoHandsService == null){
            throw new NullPointerException("TwoHandsService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<TwoHandsService>> violations = validator.validate(twoHandsService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        twoHandsServiceList.add(twoHandsService);
    }

    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
}

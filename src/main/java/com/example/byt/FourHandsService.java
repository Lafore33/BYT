package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FourHandsService extends Service{
    private boolean isExpressService;
    private final static int numOfSpecialistsRequired = 2;
    private static List<FourHandsService> fourHandsServiceList = new ArrayList<>();

    public FourHandsService(int id, String name, double regularPrice, String description,
                            double duration, boolean isExpressService) {
        super(id, name, regularPrice, description, duration);
        this.isExpressService = isExpressService;
        addFourHandsService(this);
    }

    private static void addFourHandsService(FourHandsService fourHandsService){
        if (fourHandsService == null){
            throw new NullPointerException("FourHandsService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FourHandsService>> violations = validator.validate(fourHandsService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        fourHandsServiceList.add(fourHandsService);
    }
    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
}

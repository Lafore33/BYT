package com.example.byt.models.services;

import com.example.byt.models.Material;
import com.example.byt.models.person.Master;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FourHandsService extends Service {
    private boolean isExpressService;
    private final static int numOfSpecialistsRequired = 2;
    private static List<FourHandsService> fourHandsServices = new ArrayList<>();

    public FourHandsService(int id, String name, double regularPrice, String description,
                            double duration, Set<Master> masters, boolean isExpressService) {
        super(id, name, regularPrice, description, duration, masters);
        this.isExpressService = isExpressService;
        addFourHandsService(this);
    }
    public FourHandsService(int id, String name, double regularPrice, String description,
                            double duration, Set<Master> masters, Set<Material> materialsUsed, boolean isExpressService) {
        super(id, name, regularPrice, description, duration, masters, materialsUsed);
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
        fourHandsServices.add(fourHandsService);
    }

    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
    public static List<FourHandsService> getFourHandsServiceList(){
        return new ArrayList<>(fourHandsServices);
    }

    public boolean isExpressService() {
        return isExpressService;
    }

    public static void clearExtent() {
        fourHandsServices.clear();
    }
}

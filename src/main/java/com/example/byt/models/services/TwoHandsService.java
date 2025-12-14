package com.example.byt.models.services;

import com.example.byt.models.Material;
import com.example.byt.models.person.Master;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TwoHandsService {

    private Service service;
    private final static int numOfSpecialistsRequired = 1;

    private static List<TwoHandsService> twoHandsServices = new ArrayList<>();

    protected TwoHandsService(Service service) {
        this.service = service;
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

        twoHandsServices.add(twoHandsService);
    }

    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
    public static List<TwoHandsService> getTwoHandsServiceList(){
        return new ArrayList<>(twoHandsServices);
    }

    public void removeFromExtent(){
        Service service = this.service;
        this.service = null;

        if (service != null) service.removeRelatedService();
        twoHandsServices.remove(this);
    }

    public static void clearExtent() {
        twoHandsServices.clear();
    }

    public Service getService() {
        return service;
    }
}

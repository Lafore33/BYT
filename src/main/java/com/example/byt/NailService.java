package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NailService extends Service {
    @NotNull
    private NailServiceType type;

    private boolean isCareIncluded;

    private static List<NailService> nailServiceList = new ArrayList<>();

    public NailService(int id, String name, double regularPrice, String description,
                       double duration, NailServiceType type, boolean isCareIncluded) {
        super(id, name, regularPrice, description, duration);
        this.type = type;
        this.isCareIncluded = isCareIncluded;
        addNailService(this);
    }

    private static void addNailService(NailService nailService){
        if (nailService == null){
            throw new NullPointerException("NailService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NailService>> violations = validator.validate(nailService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        nailServiceList.add(nailService);
    }
    public static List<NailService> getNailServiceList(){
        return new ArrayList<>(nailServiceList);
    }

    public NailServiceType getType() {
        return type;
    }

    public boolean isCareIncluded() {
        return isCareIncluded;
    }
}

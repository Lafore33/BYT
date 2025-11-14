package com.example.byt;

import java.util.ArrayList;
import java.util.List;

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
        fourHandsServiceList.add(fourHandsService);
    }
    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
}

package com.example.byt;

import java.util.ArrayList;
import java.util.List;

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
        twoHandsServiceList.add(twoHandsService);
    }

    public static int getNumOfSpecialistsRequired(){
        return numOfSpecialistsRequired;
    }
}

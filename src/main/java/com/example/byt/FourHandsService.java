package com.example.byt;

public class FourHandsService extends Service{
    private boolean isExpressService;
    private final static int numOfSpecialistsRequired = 2;

    public FourHandsService(int id, String name, double regularPrice, String description,
                            double duration, boolean isExpressService) {
        super(id, name, regularPrice, description, duration);
        this.isExpressService = isExpressService;
    }
}

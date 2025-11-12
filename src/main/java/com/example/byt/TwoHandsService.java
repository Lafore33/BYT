package com.example.byt;

public class TwoHandsService extends Service {
    private boolean isExpressService;
    private final static int numOfSpecialistsRequired = 1;

    public TwoHandsService(int id, String name, double regularPrice, String description,
                           double duration, double rating, double totalPrice, boolean isExpressService) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.isExpressService = isExpressService;
    }
}

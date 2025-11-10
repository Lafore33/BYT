package com.example.byt;

public class FourHandsService extends Service{
    private boolean isExpressService;
    private static int numOfSpecialistsRequired = 2;

    public FourHandsService(int id, String name, double regularPrice, String description,
                            double duration, double rating, double totalPrice, boolean isExpressService) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.isExpressService = isExpressService;
    }
}
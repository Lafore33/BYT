package com.example.byt;

public class NailService extends Service {
    private NailServiceType type;
    private boolean isCareIncluded;

    public NailService(int id, String name, double regularPrice, String description, double duration, double rating,
                       double totalPrice, NailServiceType type, boolean isCareIncluded) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.type = type;
        this.isCareIncluded = isCareIncluded;
    }

}
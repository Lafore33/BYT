package com.example.byt;

public class SkinService extends Service {

    private String purpose;

    public SkinService(int id, String name, double regularPrice, String description,
                       double duration, double rating, double totalPrice, String purpose) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.purpose = purpose;
    }
}
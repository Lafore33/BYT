package com.example.byt;

public class Service {
    private int id;
    private String name;
    private double regularPrice;
    private String description;
    private double duration;
    private double rating;
    private double totalPrice;

    public Service(int id, String name, double regularPrice, String description,
                   double duration, double rating, double totalPrice) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
        this.rating = rating;
        this.totalPrice = totalPrice;
    }
}

package com.example.byt;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class Service {

    @Min(0)
    private int id;

    @NotBlank
    private String name;

    @Min(0)
    private double regularPrice;

    @NotBlank
    private String description;

    @Min(0)
    private double duration;

    @Min(0)
    @Max(5)
    private double rating;

    @Min(0)
    private double totalPrice;

    // added default constructor for proper deserialization
    // made it protected on purpose, so that it is used only by the inheritors
    protected Service() {
    }

    private static List<Service> serviceList = new ArrayList<>();

    public Service(int id, String name, double regularPrice, String description,
                   double duration) {
        this.id = id;
        this.name = name;
        this.regularPrice = regularPrice;
        this.description = description;
        this.duration = duration;
        addService(this);
    }

    private static void addService(Service service){
        if (service == null){
            throw new NullPointerException("Service cannot be null");
        }
        serviceList.add(service);
    }
}

package com.example.byt;

import java.util.List;

public class HairService extends Service{
    private HairServiceType type;
    private List<String> hairTypes;

    public HairService(int id, String name, double regularPrice, String description, double duration, double rating,
                       double totalPrice, HairServiceType type, List<String> hairTypes) {
        super(id, name, regularPrice, description, duration, rating, totalPrice);
        this.type = type;
        this.hairTypes = hairTypes;
    }
}

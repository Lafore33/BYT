package com.example.byt;

public class TwoHandsService extends Service {
    private final static int numOfSpecialistsRequired = 1;

    public TwoHandsService(int id, String name, double regularPrice, String description,
                           double duration) {
        super(id, name, regularPrice, description, duration);
    }
}

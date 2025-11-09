package com.example.byt;

import java.util.Date;

public class Master extends Worker {
    private int experience;
    private final static int minExperienceForTop = 3;

    public Master(String name, String surname, String phoneNumber, Date birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
    }
}

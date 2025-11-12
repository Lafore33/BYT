package com.example.byt;

import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public class Master extends Worker {

    @Min(0)
    private int experience;

    private final static int minExperienceForTop = 3;

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
    }
}

package com.example.byt;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receptionist extends Worker {

    @NotNull
    private WorkType workType;

    private static List<Receptionist> receptionistList = new ArrayList<>();

    public Receptionist(String name, String surname, String phoneNumber, LocalDate birthDate, WorkType workType) {
        super(name, surname, phoneNumber, birthDate);
        this.workType = workType;
        addReceptionist(this);
    }

    private static void addReceptionist(Receptionist receptionist){
        if (receptionist == null){
            throw new NullPointerException("Receptionist cannot be null");
        }
        receptionistList.add(receptionist);
    }
}

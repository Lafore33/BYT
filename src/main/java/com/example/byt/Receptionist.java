package com.example.byt;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class Receptionist extends Worker {

    @NotNull
    private WorkType workType;

    public Receptionist(String name, String surname, String phoneNumber, LocalDate birthDate, WorkType workType) {
        super(name, surname, phoneNumber, birthDate);
        this.workType = workType;
    }
}

package com.example.byt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class Worker extends Person {

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
    }
}

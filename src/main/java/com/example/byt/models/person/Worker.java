package com.example.byt.models.person;

import java.time.LocalDate;

public abstract class Worker extends Person {

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
    }

}

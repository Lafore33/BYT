package com.example.byt.models.person;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Worker extends Person implements Serializable {

    protected Worker() {
    }

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
    }

}

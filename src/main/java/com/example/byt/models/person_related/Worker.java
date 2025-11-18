package com.example.byt.models.person_related;

import java.time.LocalDate;

abstract class Worker extends Person {

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
    }

}

package com.example.byt;

import java.util.Date;

public class Worker extends Person {
    private Date birthDate;

    public Worker(String name, String surname, String phoneNumber, Date birthDate) {
        super(name, surname, phoneNumber);
        this.birthDate = birthDate;
    }
}

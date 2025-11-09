package com.example.byt;

import java.util.Date;

public class Receptionist extends Worker {
    private WorkType workType;

    public Receptionist(String name, String surname, String phoneNumber, Date birthDate, WorkType workType) {
        super(name, surname, phoneNumber, birthDate);
        this.workType = workType;
    }
}

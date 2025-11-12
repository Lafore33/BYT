package com.example.byt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class Worker extends Person {

    @NotNull
    @Past
    private LocalDate birthDate;

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber);
        setBirthDate(birthDate);
    }

    private boolean isBirthDateValid(LocalDate birthDate) {
        return birthDate.plusYears(18).isBefore(LocalDate.now());
    }

    public void setBirthDate(LocalDate birthDate) {
        if (!isBirthDateValid(birthDate)) {
            throw new IllegalArgumentException("The worker should be at least 18 years old");
        }
        this.birthDate = birthDate;
    }
}

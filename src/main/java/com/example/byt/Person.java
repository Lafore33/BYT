package com.example.byt;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

abstract class Person {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    // '+' is allowed to be added
    @Digits(integer = 15, fraction = 0)
    @NotBlank
    private String phoneNumber;

    @Past
    private LocalDate birthDate;


    public Person(String name, String surname, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        setBirthDate(birthDate);
    }

    private boolean isBirthDateValid(LocalDate birthDate) {
        return birthDate != null && birthDate.plusYears(18).isBefore(LocalDate.now());
    }

    public void setBirthDate(LocalDate birthDate) {
        if(birthDate == null){
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (!isBirthDateValid(birthDate)) {
            throw new IllegalArgumentException("User should be at least 18 years old");
        }
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

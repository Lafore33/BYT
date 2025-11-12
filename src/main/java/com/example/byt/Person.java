package com.example.byt;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

abstract class Person {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    // '+' is allowed to be added
    @Digits(integer = 15, fraction = 0)
    @NotBlank
    private String phoneNumber;

    public Person(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
}

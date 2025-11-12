package com.example.byt;

import jakarta.validation.constraints.*;

enum CustomerStatus {
    GOOD, BAD
}

public class Customer extends Person {

    @Email
    private String emailAddress;

    @NotNull
    private CustomerStatus customerStatus;

    @Min(18)
    private int age;

    public Customer(String name, String surname, String phoneNumber, String emailAddress, int age) {
        super(name, surname, phoneNumber);
        this.emailAddress = emailAddress;
        this.age = age;
        this.customerStatus = CustomerStatus.GOOD;
    }

    public Customer(String name, String surname, String phoneNumber, int age) {
        super(name, surname, phoneNumber);
        this.age = age;
        this.customerStatus = CustomerStatus.GOOD;
    }
}

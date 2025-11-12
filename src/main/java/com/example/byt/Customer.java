package com.example.byt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

enum CustomerStatus {
    GOOD, BAD
}

public class Customer extends Person {

    @Email
    @NotBlank
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
}

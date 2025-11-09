package com.example.byt;

enum CustomerStatus {
    GOOD, BAD
}
public class Customer extends Person {

    private String emailAddress;
    private CustomerStatus customerStatus;
    private int age;

    public Customer(String name, String surname, String phoneNumber, String emailAddress, int age) {
        super(name, surname, phoneNumber);
        this.emailAddress = emailAddress;
        this.age = age;
        this.customerStatus = CustomerStatus.GOOD;
    }
}

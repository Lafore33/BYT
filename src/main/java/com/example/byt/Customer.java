package com.example.byt;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

enum CustomerStatus {
    GOOD, BAD
}

public class Customer extends Person {

    @Email
    private String emailAddress;

    @NotNull
    private CustomerStatus customerStatus;

    private static List<Customer> customers = new ArrayList<>();

    public Customer(String name, String surname, String phoneNumber, String emailAddress, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
        this.emailAddress = emailAddress;
        this.customerStatus = CustomerStatus.GOOD;
        addCustomer(this);
    }

    public Customer(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
        this.customerStatus = CustomerStatus.GOOD;
        addCustomer(this);
    }

    private static void addCustomer(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }
        customers.add(customer);
    }

    public int getAge() {
        return Period.between(getBirthDate(), LocalDate.now()).getYears();
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public String getEmailAddress() { return this.emailAddress;
    }
}

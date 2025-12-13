package com.example.byt.models.person;

import com.example.byt.models.HistoryOfStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer implements Serializable {

    @Email
    private String emailAddress;

    @NotNull
    private CustomerStatus customerStatus;

    private Set<HistoryOfStatus> historyOfStatuses = new HashSet<>();

    private static List<Customer> customers = new ArrayList<>();
    private Person person;

    protected Customer() {
    }

    protected Customer(Person person, String emailAddress) {
        if(person == null) {
            throw new NullPointerException("Person cannot be null");
        }
        this.person = person;
        this.emailAddress = emailAddress;
        this.customerStatus = CustomerStatus.GOOD;
        addCustomer(this);
    }

    public Customer(Person person) {
        if(person == null) {
            throw new NullPointerException("Person cannot be null");
        }
        this.person = person;
        this.customerStatus = CustomerStatus.GOOD;
        addCustomer(this);
    }

    private static void addCustomer(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the customer cannot be added to the list");
            return;
        }
        customers.add(customer);
    }
    protected static void removeFromExtent(Customer customer) {
        if(customer == null) {
            return;
        }
        customers.remove(customer);
    }

    public void addHistory(HistoryOfStatus historyOfStatus) {
        if (historyOfStatus == null) {
            throw new NullPointerException("historyOfStatus cannot be null");
        }

        if (this.historyOfStatuses.contains(historyOfStatus)) {
            return;
        }

        this.historyOfStatuses.add(historyOfStatus);
        historyOfStatus.addCustomer(this);
    }

    public Set<HistoryOfStatus> getHistoryOfStatuses() {
        return new HashSet<>(this.historyOfStatuses);
    }

    public void setEmailAddress(String email){
        this.emailAddress = email;
    }

    public int getAge() {
        return Period.between(person.getBirthDate(), LocalDate.now()).getYears();
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public String getEmailAddress() { return this.emailAddress;
    }

    public static List<Customer> getCustomerList() {
        return new ArrayList<>(customers);
    }

    public String getName() {
        return person.getName();
    }

    public String getSurname() {
        return person.getSurname();
    }

    public String getPhoneNumber() {
        return person.getPhoneNumber();
    }

    public LocalDate getBirthDate() {
        return person.getBirthDate();
    }

    public Person getPerson() {
        return person;
    }

    public static void clearExtent() {
        customers.clear();
    }
}

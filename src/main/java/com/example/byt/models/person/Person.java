package com.example.byt.models.person;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Person implements Serializable {

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

    private Customer customer;
    private Worker worker;

    private static List<Person> people = new ArrayList<>();

    protected Person() {
    }


    Person(String name, String surname, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        setBirthDate(birthDate);
        addPerson(this);
    }

    private static void addPerson(Person person) {
        if (person == null) {
            throw new NullPointerException("customer cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the customer cannot be added to the list");
            return;
        }
        people.add(person);
    }

    public static void removeFromExtent(Person person) {
        Customer.removeFromExtent(person.customer);
        Worker.removeFromExtent(person.worker);
        people.remove(person);
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

    public static Customer createCustomer(
            String name,
            String surname,
            String phoneNumber,
            String email,
            LocalDate birthDate) {

        Person person = new Person(name, surname, phoneNumber, birthDate);
        Customer customer = new Customer(person, email);
        person.customer = customer;
        return customer;
    }

    public static Customer createCustomer(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate) {

        Person person = new Person(name, surname, phoneNumber, birthDate);
        Customer customer = new Customer(person);
        person.customer = customer;
        return customer;
    }

    public static Worker createWorker(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate) {

        Person person = new Person(name, surname, phoneNumber, birthDate);
        Worker worker = new Worker(person);
        person.worker = worker;
        return worker;
    }

    public static Person createWorkerAndCustomer(String name,
                                                 String surname,
                                                 String phoneNumber,
                                                 String email,
                                                 LocalDate birthDate){
        Person person = new Person(name, surname, phoneNumber, birthDate);
        Customer customer = new Customer(person, email);
        Worker worker = new Worker(person);
        person.customer = customer;
        person.worker = worker;
        return person;

    }

    public static Person createWorkerAndCustomer(String name,
                                                 String surname,
                                                 String phoneNumber,
                                                 LocalDate birthDate){
        Person person = new Person(name, surname, phoneNumber, birthDate);
        Customer customer = new Customer(person);
        Worker worker = new Worker(person);
        person.customer = customer;
        person.worker = worker;
        return person;

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean isCustomer(){
        return customer != null;
    }

    public boolean isWorker(){
        return worker != null;
    }

    public Worker getWorker(){
        return worker;
    }

    public Customer getCustomer(){
        return customer;
    }
}

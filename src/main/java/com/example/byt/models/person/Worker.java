package com.example.byt.models.person;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Serializable {
    private Person person;

    private static List<Worker> workers = new ArrayList<>();

    protected Worker() {
    }

    protected Worker(Person person) {
        if(person == null){
            throw new NullPointerException("Person cannot be null");
        }
        this.person = person;
        addWorker(this);
    }

    private static void addWorker(Worker worker) {
        workers.add(worker);
    }

    protected static void removeFromExtent(Worker worker) {
        if(worker == null){
            return;
        }
        workers.remove(worker);
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

}

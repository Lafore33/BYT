package com.example.byt.models.person;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Serializable {
    private Person person;
    private Master masterRole;
    private Receptionist receptionistRole;

    private static List<Worker> workers = new ArrayList<>();

    protected Worker() {
    }

    public Worker(Person person) {
        if(person == null){
            throw new NullPointerException("Person cannot be null");
        }
        this.person = person;
        addWorker(this);
    }

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        this(new Person(name, surname, phoneNumber, birthDate));
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

    void setMasterRole(Master master) {
        if (master != null && this.receptionistRole != null) {
            throw new IllegalStateException("Worker is already a Receptionist. Use changeToMaster() first.");
        }
        this.masterRole = master;
    }

    void setReceptionistRole(Receptionist receptionist) {
        if (receptionist != null && this.masterRole != null) {
            throw new IllegalStateException("Worker is already a Master. Use changeToReceptionist() first.");
        }
        this.receptionistRole = receptionist;
    }

    void clearMasterRole() {
        this.masterRole = null;
    }

    void clearReceptionistRole() {
        this.receptionistRole = null;
    }

    public boolean isMaster() {
        return masterRole != null;
    }

    public boolean isReceptionist() {
        return receptionistRole != null;
    }

    public boolean hasRole() {
        return masterRole != null || receptionistRole != null;
    }

    public Master getMasterRole() {
        return masterRole;
    }

    public Receptionist getReceptionistRole() {
        return receptionistRole;
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

    public static List<Worker> getWorkerList() {
        return new ArrayList<>(workers);
    }
    public static void clearExtent(){
        workers.clear();
    }
}
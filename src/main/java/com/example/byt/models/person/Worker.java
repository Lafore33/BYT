package com.example.byt.models.person;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker implements Serializable {

    private Person person;

    private Master master;
    private Receptionist receptionist;

    private static List<Worker> workers = new ArrayList<>();

    protected Worker(Person person) {
        if(person == null){
            throw new NullPointerException("Person cannot be null");
        }
        this.person = person;
        workers.add(this);
    }

    public static Worker createMaster(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate,
            int experience) {
        Worker worker = Person.createWorker(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        worker.assignMaster(experience);
        return worker;
    }

    public static Worker createReceptionist(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate,
            WorkType workType
    ) {
        Worker worker = Person.createWorker(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        worker.assignInitialReceptionist(workType);
        return worker;
    }



    private void assignMaster(int experience) {
        if (this.master != null || this.receptionist != null) {
            throw new IllegalStateException("Worker already has a role");
        }
        this.master = new Master(this, experience);
    }

    private void assignInitialReceptionist(WorkType workType) {
        if (this.master != null || this.receptionist != null) {
            throw new IllegalStateException("Worker already has a role");
        }
        this.receptionist = new Receptionist(this, workType);
    }
    public boolean isMaster() {
        return master != null;
    }

    public boolean isReceptionist() {
        return receptionist != null;
    }

    public Master getMaster() {
        if (master == null) {
            throw new IllegalStateException("Worker is not a Master");
        }
        return master;
    }

    public Receptionist getReceptionist() {
        if (receptionist == null) {
            throw new IllegalStateException("Worker is not a Receptionist");
        }
        return receptionist;
    }
    public void changeToReceptionist(WorkType workType) {
        if (this.receptionist != null) {
            throw new IllegalStateException("Worker is already a Receptionist");
        }
        if (this.master == null) {
            throw new IllegalStateException("Only Master can become Receptionist");
        }

        this.master.removeMaster();
        this.master = null;
        this.receptionist = new Receptionist(this, workType);
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

    protected static void removeFromExtent(Worker worker) {
        workers.remove(worker);
    }
}

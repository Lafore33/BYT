package com.example.byt.models.person;

import com.example.byt.models.services.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Worker implements Serializable {

    private Person person;

    private Master master;
    private Receptionist receptionist;

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

    public static Master createMaster(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate,
            int experience
    ) {
        Worker worker = Person.createWorker(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        return assignMaster(worker, experience);
    }
    public static Master createMaster(
            String name,
            String surname,
            String phoneNumber,
            LocalDate birthDate,
            int experience,
            Set<Service> services
    ) {
        Worker worker = Person.createWorker(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        return assignMaster(worker, experience, services);
    }


    public static Receptionist createReceptionist(
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
        return assignInitialReceptionist(worker, workType);
    }

    public static Master assignMaster(Worker worker, int experience) {
        if (worker == null) {
            throw new NullPointerException("Worker cannot be null");
        }
        if (worker.master != null || worker.receptionist != null) {
            throw new IllegalStateException("Worker already has a role");
        }
        worker.master = new Master(worker, experience);
        return worker.master;
    }
    public static Master assignMaster(
            Worker worker,
            int experience,
            Set<Service> services
    ) {
        if (worker == null) {
            throw new NullPointerException("Worker cannot be null");
        }
        if (worker.master != null || worker.receptionist != null) {
            throw new IllegalStateException("Worker already has a role");
        }

        worker.master = new Master(worker, experience, services);
        return worker.master;
    }

    public static Receptionist assignInitialReceptionist(Worker worker, WorkType workType) {
        if (worker == null) {
            throw new NullPointerException("Worker cannot be null");
        }
        if (worker.master != null || worker.receptionist != null) {
            throw new IllegalStateException("Worker already has a role");
        }
        worker.receptionist = new Receptionist(worker, workType);
        return worker.receptionist;
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
        if (worker == null) {
            return;
        }
        if (worker.master != null) {
            worker.master.removeMaster();
            worker.master = null;
        }
        if (worker.receptionist != null) {
            worker.receptionist.removeReceptionist();
            worker.receptionist = null;
        }
        workers.remove(worker);
    }
    public static void clearExtent(){
        workers.clear();
    }

    public static List<Worker> getWorkerList() {
        return new ArrayList<>(workers);
    }

}

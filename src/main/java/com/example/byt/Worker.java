package com.example.byt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Person {

    private static List<Worker> workerList = new ArrayList<>();

    public Worker(String name, String surname, String phoneNumber, LocalDate birthDate) {
        super(name, surname, phoneNumber, birthDate);
        addWorker(this);
    }

    private static void addWorker(Worker worker){
        if (worker == null){
            throw new NullPointerException("Worker cannot be null");
        }
        workerList.add(worker);
    }

}

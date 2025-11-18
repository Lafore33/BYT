package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HistoryOfStatus {

    @NotNull
    private AppointmentStatus status;

    @NotNull
    @PastOrPresent
    private LocalDate dateOfChangingStatus;

    private static List<HistoryOfStatus> historyOfStatusList = new ArrayList<>();

    public HistoryOfStatus(AppointmentStatus status, LocalDate dateOfChangingStatus) {
        this.status = status;
        this.dateOfChangingStatus = dateOfChangingStatus;
        addHistoryOfStatus(this);
    }

    private static void addHistoryOfStatus(HistoryOfStatus historyOfStatus){
        if (historyOfStatus == null){
            throw new NullPointerException("HistoryOfStatus cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<HistoryOfStatus>> violations = validator.validate(historyOfStatus);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the history cannot be added to the list");
            return;
        }
        historyOfStatusList.add(historyOfStatus);
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public LocalDate getDateOfChangingStatus() {
        return dateOfChangingStatus;
    }

    public static List<HistoryOfStatus> getHistoryOfStatusList() {
        return new ArrayList<>(historyOfStatusList);
    }

}

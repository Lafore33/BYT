package com.example.byt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class HistoryOfStatus {

    @NotNull
    private AppointmentStatus status;

    @NotNull
    @PastOrPresent
    private LocalDate dateOfChangingStatus;

    public HistoryOfStatus(AppointmentStatus status, LocalDate dateOfChangingStatus) {
        this.status = status;
        this.dateOfChangingStatus = dateOfChangingStatus;
    }
}

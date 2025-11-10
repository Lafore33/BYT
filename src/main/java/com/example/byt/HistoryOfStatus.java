package com.example.byt;

import java.util.Date;

public class HistoryOfStatus {
    private AppointmentStatus status;
    private Date dateOfChangingStatus;

    public HistoryOfStatus(AppointmentStatus status, Date dateOfChangingStatus) {
        this.status = status;
        this.dateOfChangingStatus = dateOfChangingStatus;
    }
}

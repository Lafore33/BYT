package com.example.byt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class Appointment {
    // as the appointment might be completed, so the date will be in the past
    // but it can be scheduled as well, that mean it is in the future
    @NotNull
    private LocalDate date;

    private List<String> notes;

    @NotNull
    private PaymentMethod paymentMethod;

    @Min(0)
    private double totalPrice;

    public Appointment(LocalDate date, List<String> notes, PaymentMethod paymentMethod, double totalPrice) {
        this.date = date;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }
}

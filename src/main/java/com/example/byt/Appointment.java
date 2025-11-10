package com.example.byt;

import java.util.Date;

public class Appointment {
    private Date date;
    private String notes;
    private PaymentMethod paymentMethod;
    private double totalPrice;

    public Appointment(Date date, String notes, PaymentMethod paymentMethod, double totalPrice) {
        this.date = date;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }
}

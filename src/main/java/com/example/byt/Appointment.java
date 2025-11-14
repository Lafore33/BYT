package com.example.byt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    // as the appointment might be completed, so the date will be in the past
    // it can be scheduled as well, that mean it is in the future
    @NotNull
    private LocalDate date;

    private List<String> notes;

    private PaymentMethod paymentMethod;

    @Min(0)
    private double totalPrice;

    public Appointment(Builder builder) {
        this.date = builder.date;
        this.notes = builder.notes;
        this.paymentMethod = builder.paymentMethod;
    }

    public List<String> getNotes() {
        return notes == null ? null : new ArrayList<>(notes);
    }

    public void setNotes(List<String> notes) {
        if (notes == null) {
            this.notes = null;
            return;
        }
        if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
            throw new IllegalArgumentException("notes can't contain null or empty elements");
        }
        this.notes = new ArrayList<>(notes);
    }

    public static class Builder {
        @NotNull
        private LocalDate date;

        private List<String> notes;

        private PaymentMethod paymentMethod;

        public Builder(LocalDate date) {
            this.date = date;
        }

        public Builder notes(List<String> notes) {
            if (notes == null) {
                this.notes = null;
                return this;
            }
            if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
                throw new IllegalArgumentException("notes can't contain null or empty elements");
            }
            this.notes = new ArrayList<>(notes);
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Appointment build(){
            return new Appointment(this);
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getDate() {
        return date;
    }
}

package com.example.byt;
import jakarta.validation.constraints.*;

public class ProvidedService {
    @Min(0)
    @Max(5)
    private int rating;
    private String comment;
    @Min(0)
    private double time;
    @Min(0)
    private double price;

    public ProvidedService(int rating, String comment, double time, double price) {
        this.rating = rating;
        this.comment = comment;
        this.time = time;
        this.price = price;
    }
}

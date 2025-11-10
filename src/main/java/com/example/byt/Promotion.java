package com.example.byt;

import java.util.Date;

public class Promotion {
    private String name;
    private String description;
    private double percentage;
    private Date startDate;
    private Date endDate;

    public Promotion(String name, String description, double percentage, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

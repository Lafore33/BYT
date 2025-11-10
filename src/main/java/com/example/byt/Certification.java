package com.example.byt;

import java.util.Date;

public class Certification {
    private String name;
    private String certificationNumber;
    private String description;
    private String organization;
    private Date issueDate;
    private Date expiryDate;

    public Certification(String name, String certificationNumber, String description,
                         String organization, Date issueDate, Date expiryDate) {
        this.name = name;
        this.certificationNumber = certificationNumber;
        this.description = description;
        this.organization = organization;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }
}

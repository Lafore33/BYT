package com.example.byt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class Certification {
    @NotBlank
    private String name;

    @NotBlank
    private String certificationNumber;

    @NotBlank
    private String description;

    @NotBlank
    private String organization;

    @PastOrPresent
    @NotNull
    private LocalDate issueDate;

    private LocalDate expiryDate;

    public Certification(String name, String certificationNumber, String description,
                         String organization, LocalDate issueDate, LocalDate expiryDate) {
        this.name = name;
        this.certificationNumber = certificationNumber;
        this.description = description;
        this.organization = organization;
        this.issueDate = issueDate;
        setExpiryDate(expiryDate);
    }

    public Certification(String name, String certificationNumber, String description,
                         String organization, LocalDate issueDate) {
        this.name = name;
        this.certificationNumber = certificationNumber;
        this.description = description;
        this.organization = organization;
        this.issueDate = issueDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && issueDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.expiryDate = expiryDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        if (expiryDate != null && issueDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.issueDate = issueDate;
    }

}

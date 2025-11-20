package com.example.byt.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    private static List<Certification> certifications = new ArrayList<>();

    public Certification(String name, String certificationNumber, String description,
                         String organization, LocalDate issueDate, LocalDate expiryDate) {
        this.name = name;
        this.certificationNumber = certificationNumber;
        this.description = description;
        this.organization = organization;
        setIssueDate(issueDate);
        setExpiryDate(expiryDate);
        addCertification(this);
    }


    public Certification(String name, String certificationNumber, String description,
                         String organization, LocalDate issueDate) {
        this.name = name;
        this.certificationNumber = certificationNumber;
        this.description = description;
        this.organization = organization;
        setIssueDate(issueDate);
        addCertification(this);
    }

    private static void addCertification(Certification certification) {
        if (certification == null) {
            throw new NullPointerException("certification cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the cetification cannot be added to the list");
            return;
        }
        certifications.add(certification);
    }

    public static List<Certification> getCertificationList() {
        return new ArrayList<>(certifications);
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && issueDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.expiryDate = expiryDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        if (issueDate == null) {
            throw new IllegalArgumentException("issueDate cannot be null");
        }
        if (expiryDate != null && issueDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException("expiryDate must be after issueDate");
        }
        this.issueDate = issueDate;
    }

    public String getName() {
        return name;
    }

    public String getCertificationNumber() {
        return certificationNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganization() {
        return organization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public static void clearExtent() {
        certifications.clear();
    }
}

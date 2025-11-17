package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CertificationTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCertificationShouldHaveNoViolations() {
        Certification certification = new Certification(
                "Manicure course",
                "CERT-123",
                "Professional manicure course",
                "Beauty school",
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(violations.isEmpty(),
                "Valid certification should have no violations, but got: " + violations);
    }

    @Test
    void nullNameShouldFailValidation() {
        Certification certification = new Certification(
                null,
                "CERT-123",
                "Description",
                "Organization",
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "name"), "Expected violation for null 'name', but got: " + violations);
    }

    @Test
    void blankNameShouldFailValidation() {
        Certification certification = new Certification(
                "   ",
                "CERT-123",
                "Description",
                "Organization",
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "name"), "Expected violation for blank 'name', but got: " + violations);
    }

    @Test
    void nullCertificationNumberShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                null,
                "Description",
                "Organization",
                LocalDate.now(),
                null
        );

        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "certificationNumber"), "Expected violation for null 'certificationNumber', but got: " + violations);
    }

    @Test
    void blankCertificationNumberShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "   ",
                "Description",
                "Organization",
                LocalDate.now(),
                null
        );

        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "certificationNumber"), "Expected violation for blank 'certificationNumber', but got: " + violations);
    }

    @Test
    void nullDescriptionShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                null,
                "Organization",
                LocalDate.now(),
                null
        );

        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "description"), "Expected violation for null 'description', but got: " + violations);

    }

    @Test
    void blankDescriptionShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "   ",
                "Organization",
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "description"), "Expected violation for blank 'description', but got: " + violations);
    }

    @Test
    void nullOrganizationShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                null,
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "organization"), "Expected violation for null 'organization', but got: " + violations);
    }

    @Test
    void blankOrganizationShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                "   ",
                LocalDate.now(),
                null
        );

        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "organization"), "Expected violation for blank 'organization', but got: " + violations);
    }

    @Test
    void nullIssueDateShouldFailValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Certification(
                    "Course",
                    "CERT-123",
                    "Description",
                    "Organization",
                    null,
                    null
            );
        }, "IllegalArgumentException expected for null 'issueDate'");
    }

    @Test
    void futureIssueDateShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                LocalDate.now().plusDays(1),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "issueDate"), "Expected violation for future 'issueDate', but got: " + violations);
    }
    @Test
    void expiryDateBeforeIssueDateShouldFail() {
        LocalDate issue = LocalDate.now();
        LocalDate expiry = issue.minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
            new Certification(
                    "Course",
                    "CERT-123",
                    "Description",
                    "Organization",
                    issue,
                    expiry
            ), "Expected IllegalArgumentException when expiryDate is before issueDate");
    }

    @Test
    void expiryDateEqualToIssueDateShouldPass() {
        LocalDate date = LocalDate.now();

        assertDoesNotThrow(() -> new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                date,
                date
        ), "Should allow expiryDate equal to issueDate");
    }

    @Test
    void expiryDateAfterIssueDateShouldPass() {
        LocalDate issue = LocalDate.now();
        LocalDate expiry = issue.plusDays(30);

        assertDoesNotThrow(() -> new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                issue,
                expiry
        ), "Should allow expiryDate after issueDate");
    }

    @Test
    void settingExpiryDateAfterIssueDateShouldPass() {
        Certification cert = new Certification(
                "Course",
                "CERT-004",
                "Description",
                "Org",
                LocalDate.now()
        );

        LocalDate expiry = LocalDate.now().plusDays(10);
        assertDoesNotThrow(() -> cert.setExpiryDate(expiry),
                "Should allow setting expiryDate after issueDate");
    }

    @Test
    void settingExpiryDateBeforeIssueDateShouldFail() {
        Certification cert = new Certification(
                "Course",
                "CERT-005",
                "Description",
                "Org",
                LocalDate.now()
        );

        LocalDate expiry = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> cert.setExpiryDate(expiry),
                "Expected exception when setting expiryDate before issueDate");
    }


    private boolean containsViolationFor(Set<ConstraintViolation<Certification>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}

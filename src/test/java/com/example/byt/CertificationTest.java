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
//I forgot to create a separate branch before starting the work,
// so I had to create this branch after all tests were already
// implemented and then move the finished changes here.
// That’s why the time gap between writing the tests and creating the branch is so small.
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

        assertTrue(containsViolationFor(validator.validate(certification), "name"));
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

        assertTrue(containsViolationFor(validator.validate(certification), "name"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "certificationNumber"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "certificationNumber"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "description"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "description"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "organization"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "organization"));
    }

    @Test
    void nullIssueDateShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                null,
                null
        );
        assertTrue(containsViolationFor(validator.validate(certification), "issueDate"));
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
        assertTrue(containsViolationFor(validator.validate(certification), "issueDate"));
    }

    private boolean containsViolationFor(Set<? extends ConstraintViolation<?>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}

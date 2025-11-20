package com.example.byt;

import com.example.byt.models.Certification;
import com.example.byt.models.services.HairService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CertificationTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void clearExtent() {
        Certification.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        String name = "Manicure course";
        String certificationNumber = "CERT-123";
        String description = "Professional manicure course";
        String organization = "Beauty school";
        LocalDate issueDate = LocalDate.of(2020, 1, 1);
        LocalDate expiryDate = LocalDate.of(2020, 12, 31);
        Certification certification = new Certification(
                name,
                certificationNumber,
                description,
                organization,
                issueDate,
                expiryDate
        );
        assertEquals(name, certification.getName(), "Incorrect name set in the constructor");
        assertEquals(certificationNumber, certification.getCertificationNumber(), "Incorrect certification number set in the constructor");
        assertEquals(description, certification.getDescription(), "Incorrect description set in the constructor");
        assertEquals(organization, certification.getOrganization(), "Incorrect organization set in the constructor");
        assertEquals(issueDate, certification.getIssueDate(), "Incorrect issue date set in the constructor");
        assertEquals(expiryDate, certification.getExpiryDate(), "Incorrect expiry date set in the constructor");
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
        assertTrue(violations.isEmpty(),"Valid certification should have no violations, but got: " + violations);
        assertTrue(Certification.getCertificationList().contains(certification),"Valid certification must be added to extent");
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
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for null 'name', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "name"),"Expected violation for blank 'name', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "certificationNumber"),"Expected violation for null 'certificationNumber', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "certificationNumber"),"Expected violation for blank 'certificationNumber', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "description"),"Expected violation for null 'description', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "description"),"Expected violation for blank 'description', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
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
        assertTrue(containsViolationFor(violations, "organization"),"Expected violation for null 'organization', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
    }

    @Test
    void blankOrganizationShouldFailValidation() {
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description","   ",
                LocalDate.now(),
                null
        );
        Set<ConstraintViolation<Certification>> violations = validator.validate(certification);
        assertTrue(containsViolationFor(violations, "organization"),"Expected violation for blank 'organization', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
    }

    @Test
    void nullIssueDateShouldThrowException() {
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
        List<Certification> list = Certification.getCertificationList();
        assertTrue(list.isEmpty(), "The invalid certification should not be added to the list");
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
        assertTrue(containsViolationFor(violations, "issueDate"),"Expected violation for future 'issueDate', but got: " + violations);
        assertFalse(Certification.getCertificationList().contains(certification),"Invalid certification must NOT be added to extent");
    }

    @Test
    void expiryDateBeforeIssueDateShouldThrowException() {
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
        List<Certification> list = Certification.getCertificationList();
        assertTrue(list.isEmpty(), "The invalid certification should not be added to the list");
    }

    @Test
    void expiryDateEqualToIssueDateShouldNotThrowException() {
        LocalDate date = LocalDate.now();
        assertDoesNotThrow(() -> new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                date,
                date
        ), "Should allow expiryDate equal to issueDate");
        assertFalse(Certification.getCertificationList().isEmpty(),"Valid certification must be added to extent");
    }

    @Test
    void expiryDateAfterIssueDateShouldNotThrowException() {
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
        assertFalse(Certification.getCertificationList().isEmpty(),"Valid certification must be added to extent");
    }

    @Test
    void setExpiryDateSetsValuesCorrectly(){
        LocalDate issue = LocalDate.now();
        LocalDate expiry = issue.plusDays(30);
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                issue
        );
        certification.setExpiryDate(expiry);
        assertEquals(expiry, certification.getExpiryDate());
    }

    @Test
    void settingExpiryDateAfterIssueDateShouldNotThrowException() {
        Certification cert = new Certification(
                "Course",
                "CERT-004",
                "Description",
                "Org",
                LocalDate.now()
        );
        LocalDate expiry = LocalDate.now().plusDays(10);
        assertDoesNotThrow(() -> cert.setExpiryDate(expiry),"Should allow setting expiryDate after issueDate");
    }

    @Test
    void settingExpiryDateBeforeIssueDateShouldThrowException() {
        Certification cert = new Certification(
                "Course",
                "CERT-005",
                "Description",
                "Org",
                LocalDate.now()
        );
        LocalDate expiry = LocalDate.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> cert.setExpiryDate(expiry),"Expected exception when setting expiryDate before issueDate");
    }

    @Test
    void setIssueDateSetsValuesCorrectly(){
        LocalDate issue = LocalDate.now().minusDays(25);
        Certification certification = new Certification(
                "Course",
                "CERT-123",
                "Description",
                "Organization",
                LocalDate.now()
        );
        certification.setIssueDate(issue);
        assertEquals(issue, certification.getIssueDate());
    }

    @Test
    void settingIssueDateNullShouldThrowException() {
        Certification cert = new Certification(
                "Course",
                "CERT-005",
                "Description",
                "Org",
                LocalDate.now()
        );
        assertThrows(IllegalArgumentException.class, () -> cert.setIssueDate(null),"Expected exception when setting issueDate null");
    }

    @Test
    void settingIssueDateAfterExpiryDateShouldThrowException() {
        Certification cert = new Certification(
                "Course",
                "CERT-005",
                "Description",
                "Org",
                LocalDate.of(2024, 10, 26),
                LocalDate.of(2025, 10, 26)
        );
        LocalDate newIssueDate = LocalDate.of(2025, 11, 26);
        assertThrows(IllegalArgumentException.class, () -> cert.setIssueDate(newIssueDate),"Expected exception when setting issueDate after expiryDate");
    }

    @Test
    void settingIssueDateBeforeExpiryDateShouldNotThrowException() {
        Certification cert = new Certification(
                "Course",
                "CERT-005",
                "Description",
                "Org",
                LocalDate.of(2024, 10, 26),
                LocalDate.of(2025, 10, 26)
        );
        LocalDate newIssueDate = LocalDate.of(2025, 9, 26);
        assertDoesNotThrow(() -> cert.setIssueDate(newIssueDate),"Expected no exception when setting issueDate before expiryDate");
    }

    @Test
    void getCertificationListShouldReturnCopy() {
        Certification certification = new Certification(
                "Course",
                "CERT-999",
                "Description",
                "Organization",
                LocalDate.now(),
                null
        );
        List<Certification> listCopy = Certification.getCertificationList();
        listCopy.clear();
        List<Certification> originalList = Certification.getCertificationList();
        assertTrue(originalList.contains(certification),"Original list must not be modified when copy is cleared");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Certification>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
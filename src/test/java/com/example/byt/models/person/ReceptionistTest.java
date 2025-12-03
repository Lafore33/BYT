package com.example.byt.models.person;

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

class ReceptionistTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void clearExtent() {
        Receptionist.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectly(){
        String name = "Yelizaveta";
        String surname = "Gaiduk";
        String phoneNumber = "+48123456789";
        WorkType type = WorkType.FULL_TIME;
        LocalDate birthDate = LocalDate.now().minusYears(25);

        Receptionist receptionist = new Receptionist(
                name,
                surname,
                phoneNumber,
                birthDate,
                type
        );

        assertEquals(name, receptionist.getName(), "Incorrect name set in constructor");
        assertEquals(surname, receptionist.getSurname(), "Incorrect surname set in constructor");
        assertEquals(phoneNumber, receptionist.getPhoneNumber(), "Incorrect phone number set in constructor");
        assertEquals(birthDate, receptionist.getBirthDate(), "Incorrect birth date set in constructor");
        assertEquals(type, receptionist.getWorkType(), "Incorrect work type set in constructor");
    }

    @Test
    void validReceptionistPassesValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Receptionist receptionist = new Receptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                WorkType.FULL_TIME
        );
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        assertTrue(violations.isEmpty(),"Valid receptionist should have no validation violations");
        assertTrue(Receptionist.getReceptionistList().contains(receptionist), "Valid receptionist must be added to extent");
    }

    @Test
    void nullWorkTypeFailsValidation() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Receptionist receptionist = new Receptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                null
        );
        Set<ConstraintViolation<Receptionist>> violations = validator.validate(receptionist);
        assertTrue(containsViolationFor(violations, "workType"),"Expected violation for null 'workType', but got: " + violations);
        assertFalse(Receptionist.getReceptionistList().contains(receptionist), "Invalid receptionist must NOT be added to extent");
    }

    @Test
    void getReceptionistListShouldReturnCopy() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Receptionist receptionist = new Receptionist(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                birthDate,
                WorkType.FULL_TIME
        );
        List<Receptionist> listCopy = Receptionist.getReceptionistList();
        listCopy.clear();
        List<Receptionist> originalList = Receptionist.getReceptionistList();
        assertTrue(originalList.contains(receptionist),"Original list must NOT be modified when copy is cleared");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Receptionist>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
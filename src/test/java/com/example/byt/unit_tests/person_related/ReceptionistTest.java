package com.example.byt.unit_tests.person_related;

import com.example.byt.models.person_related.Receptionist;
import com.example.byt.models.person_related.WorkType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionistTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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
        assertEquals(surname, receptionist.getSurname(), "Incorrect name set in constructor");
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
        assertTrue(violations.isEmpty(), "Valid receptionist should have no validation violations");
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
        assertTrue(containsViolationFor(violations, "workType"),
                "Expected violation for null 'workType', but got: " + violations);
    }
    private boolean containsViolationFor(Set<ConstraintViolation<Receptionist>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
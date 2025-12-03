package com.example.byt.models.person;
import com.example.byt.models.person.Master;
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

class MasterTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void clearExtent() {
        Master.clearExtent();
    }

    @Test
    void constructorSetsValuesCorrectly() {
        String name = "Yelizaveta";
        String surname = "Gaiduk";
        String phoneNumber = "+48123456789";
        LocalDate dateOfBirth = LocalDate.now().minusYears(25);
        int experience = 5;

        Master master = new Master(
                name,
                surname,
                phoneNumber,
                dateOfBirth,
                experience
        );
        assertEquals(name, master.getName(), "Incorrect name set in constructor");
        assertEquals(surname, master.getSurname(), "Incorrect surname set in constructor");
        assertEquals(phoneNumber, master.getPhoneNumber(), "Incorrect phone number set in constructor");
        assertEquals(dateOfBirth, master.getBirthDate(), "Incorrect birth date set in constructor");
        assertEquals(experience, master.getExperience(), "Incorrect experience set in constructor");
    }

    @Test
    void validMasterShouldHaveNoViolations() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                5
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(),"Valid master should have no validation violations");
        assertTrue(Master.getMasterList().contains(master),"Valid object must be added to extent");
    }

    @Test
    void zeroExperienceShouldBeValid() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                0
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(violations.isEmpty(),"Zero experience should be valid");
        assertTrue(Master.getMasterList().contains(master),"Valid master should be added to extent");
    }

    @Test
    void negativeExperienceShouldFailValidation() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456789",
                LocalDate.now().minusYears(25),
                -1
        );
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        assertTrue(containsViolationFor(violations, "experience"),"Expected violation for negative experience");
        assertFalse(Master.getMasterList().contains(master),"Invalid master must NOT be added to extent");
    }

    @Test
    void getMinExperienceForTopReturnsCorrectValue() {
        assertEquals(3, Master.getMinExperienceForTop(), "Incorrect min experience for top");
    }

    @Test
    void getMasterListShouldReturnCopy() {
        Master master = new Master(
                "Yelizaveta",
                "Gaiduk",
                "+48123456777",
                LocalDate.now().minusYears(25),
                3
        );
        List<Master> listCopy = Master.getMasterList();listCopy.clear();
        List<Master> originalList = Master.getMasterList();
        assertTrue(originalList.contains(master),"Original list must NOT be modified when copy is cleared");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Master>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
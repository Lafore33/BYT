package com.example.byt.person;
import com.example.byt.models.ProvidedService;
import com.example.byt.models.appointment.Appointment;
import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        ProvidedService.clearExtent();
        Service.clearExtent();
        Appointment.clearExtent();
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

    @Test
    void addProvidedServiceCreatesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .service(service)
                .appointment(appointment)
                .build();
        master2.addProvidedService(ps);
        assertTrue(master2.getProvidedServices().contains(ps),
                "Master2 should contain PS (direct check)");
        assertTrue(ps.getMasters().contains(master2),
                "PS should contain master2 (reverse connection check)");
        assertTrue(master1.getProvidedServices().contains(ps),
                "Master1 should still contain PS");
        assertTrue(ps.getMasters().contains(master1),
                "PS should still contain master1");
    }

    @Test
    void removeProvidedServiceRemovesReverseConnection() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .addMaster(master2)
                .service(service)
                .appointment(appointment)
                .build();
        master1.removeProvidedService(ps);
        assertFalse(master1.getProvidedServices().contains(ps),
                "Master1 should NOT contain PS (direct check)");
        assertFalse(ps.getMasters().contains(master1),
                "PS should NOT contain master1 (reverse connection check)");
        assertTrue(master2.getProvidedServices().contains(ps),
                "Master2 should still contain PS");
        assertTrue(ps.getMasters().contains(master2),
                "PS should still contain master2");
    }

    @Test
    void removeLastMasterFromProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        assertThrows(IllegalStateException.class, () -> master.removeProvidedService(ps),
                "Removing the last master should throw IllegalStateException (multiplicity 1..2)");
        assertTrue(master.getProvidedServices().contains(ps),
                "Master should still contain PS after failed removal");
        assertTrue(ps.getMasters().contains(master),
                "PS should still contain master after failed removal");
    }
    @Test
    void addNullProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        assertThrows(IllegalArgumentException.class, () -> master.addProvidedService(null));
    }

    @Test
    void addDuplicateProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();
        assertThrows(IllegalArgumentException.class, () -> master.addProvidedService(ps));
    }

    @Test
    void removeNullProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        assertThrows(IllegalArgumentException.class, () -> master.removeProvidedService(null));
    }

    @Test
    void removeNonExistentProvidedServiceThrowsException() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master1 = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Master master2 = new Master("Jane", "Smith", "+48222222222", birthDate, 7);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master1)
                .service(service)
                .appointment(appointment)
                .build();

        assertThrows(IllegalArgumentException.class, () -> master2.removeProvidedService(ps));
    }
    @Test
    void getProvidedServicesReturnsDefensiveCopy() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Master master = new Master("John", "Doe", "+48111111111", birthDate, 5);
        Service service = new Service(1, "Manicure", 50.0, "Professional manicure", 60.0);
        Appointment appointment = new Appointment.Builder(LocalDate.of(2025, 12, 15)).build();
        ProvidedService ps = new ProvidedService.Builder(LocalDateTime.of(2025, 12, 15, 14, 0))
                .addMaster(master)
                .service(service)
                .appointment(appointment)
                .build();

        Set<ProvidedService> copy = master.getProvidedServices();
        copy.clear();
        assertEquals(1, master.getProvidedServices().size(), "Original set should not be modified");
        assertTrue(master.getProvidedServices().contains(ps), "Original set should still contain ps");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Master>> violations, String fieldName) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}
package com.example.byt.person;

import com.example.byt.models.person.Worker;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    private static class TestWorker extends Worker {
        public TestWorker(String name, String surname, String phoneNumber, LocalDate birthDate) {
            super(name, surname, phoneNumber, birthDate);
        }
    }

    @Test
    void constructorSetsValuesCorrectly() {
        String name = "Yelizaveta";
        String surname = "Gaiduk";
        String phoneNumber = "+48123456789";
        LocalDate birthDate = LocalDate.now().minusYears(25);

        TestWorker worker = new TestWorker(
                name,
                surname,
                phoneNumber,
                birthDate
        );
        assertEquals(name, worker.getName(), "Incorrect name set in the constructor");
        assertEquals(surname, worker.getSurname(), "Incorrect surname set in the constructor");
        assertEquals(phoneNumber, worker.getPhoneNumber(), "Incorrect phoneNumber set in the constructor");
        assertEquals(birthDate, worker.getBirthDate(), "Incorrect birthDate set in the constructor");
    }
}
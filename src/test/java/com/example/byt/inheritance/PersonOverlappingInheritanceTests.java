package com.example.byt.inheritance;

import com.example.byt.models.person.Customer;
import com.example.byt.models.person.Person;
import com.example.byt.models.person.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonOverlappingInheritanceTests {

    @BeforeEach
    void clearExtent() {
        Customer.clearExtent();
        Person.clearExtent();
        Worker.clearExtent();
    }

    @Test
    void testCreateCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Customer customer = Person.createCustomer("John", "Doe", "123456789", "john@example.com", birthDate);

        Person person = customer.getPerson();
        assertNotNull(person);
        assertTrue(person.isCustomer());
        assertFalse(person.isWorker());

        assertEquals(customer, person.getCustomer());
        assertNull(person.getWorker());
    }

    @Test
    void testCreateWorker() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Worker worker = Person.createWorker("Jane", "Smith", "987654321", birthDate);

        Person person = worker.getPerson();
        assertNotNull(person);
        assertTrue(person.isWorker());
        assertFalse(person.isCustomer());

        assertEquals(worker, person.getWorker());
        assertNull(person.getCustomer());
    }

    @Test
    void testCreatePersonWithBothRoles() {
        LocalDate birthDate = LocalDate.now().minusYears(40);
        Person person = Person.createWorkerAndCustomer("Alice", "Johnson", "555123456", "alice@example.com", birthDate );

        assertTrue(person.isCustomer());
        assertTrue(person.isWorker());

        Customer customer = person.getCustomer();
        Worker worker = person.getWorker();

        assertNotNull(customer);
        assertNotNull(worker);

        assertEquals(person, customer.getPerson());
        assertEquals(person, worker.getPerson());
    }

    @Test
    void testRemovePersonAlsoRemovesAssociations() {
        LocalDate birthDate = LocalDate.now().minusYears(22);
        Person person = Person.createWorkerAndCustomer("Bob", "Brown", "444555666", "bob@example.com", birthDate );

        assertTrue(person.isCustomer());
        assertTrue(person.isWorker());

        Person.removeFromExtent(person);

        assertTrue(Customer.getCustomerList().isEmpty());
        assertTrue(Worker.getWorkerList().isEmpty());
        assertTrue(Person.getPersonList().isEmpty());
    }

    @Test
    void testAddWorkerToCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        Customer customer = Person.createCustomer("John", "Doe", "123456789", "john@example.com", birthDate );

        Worker worker = Person.addWorkerToCustomer(customer);

        assertNotNull(worker);
        assertEquals(customer.getPerson(), worker.getPerson());
        assertTrue(worker.getPerson().isWorker());
        assertEquals(worker, customer.getPerson().getWorker());
    }

    @Test
    void testAddCustomerToWorker() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        Worker worker = Person.createWorker("Jane", "Smith", "987654321", birthDate);

        Customer customer = Person.addCustomerToWorker(worker);

        assertNotNull(customer);
        assertEquals(worker.getPerson(), customer.getPerson());
        assertTrue(customer.getPerson().isCustomer());
        assertEquals(customer, worker.getPerson().getCustomer());
    }

    @Test
    void testAddWorkerToCustomerWhenAlreadyWorker() {
        LocalDate birthDate = LocalDate.now().minusYears(28);
        Person person = Person.createWorkerAndCustomer("Alice", "Johnson", "555123456", "alice@example.com", birthDate);

        Customer customer = person.getCustomer();

        assertThrows(IllegalStateException.class, () -> Person.addWorkerToCustomer(customer));
    }

    @Test
    void testAddCustomerToWorkerWhenAlreadyCustomer() {
        LocalDate birthDate = LocalDate.now().minusYears(35);
        Person person = Person.createWorkerAndCustomer("Bob", "Brown", "444555666", "bob@example.com", birthDate );

        Worker worker = person.getWorker();

        assertThrows(IllegalStateException.class, () -> Person.addCustomerToWorker(worker));
    }
}


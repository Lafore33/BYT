package com.example.byt.inheritance;

import com.example.byt.models.person.Master;
import com.example.byt.models.person.Receptionist;
import com.example.byt.models.person.Worker;
import com.example.byt.models.person.WorkType;
import com.example.byt.models.person.Person;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkerDynamicInheritanceTest {

    @BeforeEach
    void clearExtent() {
        Service.clearExtent();
        Master.clearExtent();
        Receptionist.clearExtent();
        Worker.clearExtent();
        Person.clearExtent();
    }

    @Test
    void testCreateMaster() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Master master = Worker.createMaster("Yelizaveta", "Gaiduk", "512345678", birthDate, 5);

        Worker worker = master.getWorker();
        assertNotNull(worker);
        assertTrue(worker.isMaster());
        assertFalse(worker.isReceptionist());

        assertEquals(master, worker.getMaster());
    }

    @Test
    void testCreateReceptionist() {
        LocalDate birthDate = LocalDate.now().minusYears(19);
        Receptionist receptionist = Worker.createReceptionist("Dana", "Nazarchuk", "523456789", birthDate, WorkType.FULL_TIME);

        Worker worker = receptionist.getWorker();
        assertNotNull(worker);
        assertTrue(worker.isReceptionist());
        assertFalse(worker.isMaster());

        assertEquals(receptionist, worker.getReceptionist());
    }

    @Test
    void testWorkerCannotHaveBothRolesSimultaneously() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Master master = Worker.createMaster("Margo", "Bilyk", "534567890", birthDate, 3);

        Worker worker = master.getWorker();

        assertThrows(IllegalStateException.class, () -> Worker.assignInitialReceptionist(worker, WorkType.PART_TIME));
    }

    @Test
    void testAssignMasterToExistingWorkerWithReceptionist() {
        LocalDate birthDate = LocalDate.now().minusYears(19);
        Receptionist receptionist = Worker.createReceptionist("Mikhail", "Koutun", "545678901", birthDate, WorkType.FULL_TIME);

        Worker worker = receptionist.getWorker();

        assertThrows(IllegalStateException.class, () -> Worker.assignMaster(worker, 2));
    }

    @Test
    void testChangeFromMasterToReceptionist() {
        LocalDate birthDate = LocalDate.now().minusYears(20);

        Master master1 = Worker.createMaster("Yelizaveta", "Gaiduk", "556789012", birthDate, 4);
        Master master2 = Worker.createMaster("Dana", "Nazarchuk", "567890123", birthDate, 3);

        Service service = new Service(1, "Service", 100, "Description", 60, Set.of(master1, master2));

        Worker worker = master1.getWorker();

        assertTrue(worker.isMaster());
        assertFalse(worker.isReceptionist());

        worker.changeToReceptionist(WorkType.FULL_TIME);

        assertFalse(worker.isMaster());
        assertTrue(worker.isReceptionist());

        Receptionist receptionist = worker.getReceptionist();
        assertNotNull(receptionist);
        assertEquals(WorkType.FULL_TIME, receptionist.getWorkType());
        assertEquals(worker, receptionist.getWorker());
    }

    @Test
    void testExtentsAfterRoleChange() {
        LocalDate birthDate = LocalDate.now().minusYears(19);

        Master master1 = Worker.createMaster("Margo", "Bilyk", "578901234", birthDate, 4);
        Master master2 = Worker.createMaster("Mikhail", "Koutun", "589012345", birthDate, 3);

        Service service = new Service(1, "Manicure", 100, "Nail service", 60, Set.of(master1, master2));

        assertEquals(2, Master.getMasterList().size());
        assertEquals(0, Receptionist.getReceptionistList().size());

        Worker worker = master1.getWorker();
        worker.changeToReceptionist(WorkType.PART_TIME);

        assertEquals(1, Master.getMasterList().size());
        assertEquals(1, Receptionist.getReceptionistList().size());
    }

    @Test
    void testChangeToReceptionistWhenAlreadyReceptionist() {
        LocalDate birthDate = LocalDate.now().minusYears(19);
        Receptionist receptionist = Worker.createReceptionist("Margo", "Bilyk", "578901234", birthDate, WorkType.PART_TIME);

        Worker worker = receptionist.getWorker();

        assertThrows(IllegalStateException.class, () -> worker.changeToReceptionist(WorkType.FULL_TIME));
    }

    @Test
    void testChangeToReceptionistWhenNotMaster() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Worker worker = Person.createWorker("Mikhail", "Koutun", "589012345", birthDate);

        assertThrows(IllegalStateException.class, () -> worker.changeToReceptionist(WorkType.FULL_TIME));
    }

    @Test
    void testRemoveWorkerAlsoRemovesMaster() {
        LocalDate birthDate = LocalDate.now().minusYears(19);

        Master master1 = Worker.createMaster("Yelizaveta", "Gaiduk", "590123456", birthDate, 5);
        Master master2 = Worker.createMaster("Dana", "Nazarchuk", "601234567", birthDate, 4);

        Service service = new Service(1, "Service", 100, "Description", 60, Set.of(master1, master2));

        Worker worker = master1.getWorker();
        Person person = worker.getPerson();

        assertTrue(worker.isMaster());
        assertEquals(2, Master.getMasterList().size());

        Person.removeFromExtent(person);

        assertEquals(1, Master.getMasterList().size());
        assertEquals(1, Worker.getWorkerList().size());
        assertEquals(1, Person.getPersonList().size());
    }

    @Test
    void testRemoveWorkerAlsoRemovesReceptionist() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Receptionist receptionist = Worker.createReceptionist("Margo", "Bilyk", "612345678", birthDate, WorkType.FULL_TIME);

        Worker worker = receptionist.getWorker();
        Person person = worker.getPerson();

        assertTrue(worker.isReceptionist());
        assertFalse(Receptionist.getReceptionistList().isEmpty());

        Person.removeFromExtent(person);

        assertTrue(Receptionist.getReceptionistList().isEmpty());
        assertTrue(Worker.getWorkerList().isEmpty());
        assertTrue(Person.getPersonList().isEmpty());
    }

    @Test
    void testAssignMasterToNullWorkerThrowsException() {
        assertThrows(NullPointerException.class, () -> Worker.assignMaster(null, 3));
    }

    @Test
    void testAssignReceptionistToNullWorkerThrowsException() {
        assertThrows(NullPointerException.class, () -> Worker.assignInitialReceptionist(null, WorkType.FULL_TIME));
    }
}
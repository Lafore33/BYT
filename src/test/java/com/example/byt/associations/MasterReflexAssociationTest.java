package com.example.byt.associations;

import com.example.byt.models.person.Master;
import com.example.byt.models.person.Worker;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MasterReflexAssociationTest {

    private Master topManager;
    private Master trainee1;
    private Master trainee2;
    private Master anotherTopManager;
    private Master nonTop1;
    private Master nonTop2;

    @BeforeEach
    void setUp() {
        Service.clearExtent();
        Master.clearExtent();

        topManager = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111222", LocalDate.of(2005, 11, 15), 5).getMaster();
        trainee1 = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111223", LocalDate.of(2005, 11, 15), 1).getMaster();
        trainee2 = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111224", LocalDate.of(2005, 11, 15), 2).getMaster();
        anotherTopManager = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111225", LocalDate.of(2005, 11, 15), 4).getMaster();
        nonTop1 = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111226", LocalDate.of(2005, 11, 15), 1).getMaster();
        nonTop2 = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111227", LocalDate.of(2005, 11, 15), 2).getMaster();
    }

    @Test
    void setManagerUpdatesBothSides() {
        trainee1.setManager(topManager);

        assertEquals(topManager, trainee1.getManager());
        assertTrue(topManager.getTrainees().contains(trainee1));
        assertTrue(trainee1.hasManager());
        assertTrue(topManager.hasTrainees());
        assertEquals(1, topManager.getTraineeCount());
        assertTrue(topManager.isTrainee(trainee1));
    }

    @Test
    void addTraineeUpdatesBothSides() {
        topManager.addTrainee(trainee1);

        assertEquals(topManager, trainee1.getManager());
        assertTrue(topManager.getTrainees().contains(trainee1));
        assertTrue(trainee1.hasManager());
        assertTrue(topManager.hasTrainees());
        assertEquals(1, topManager.getTraineeCount());
        assertTrue(topManager.isTrainee(trainee1));
    }

    @Test
    void setManagerToNullRemovesAssociations() {
        trainee1.setManager(topManager);
        trainee1.setManager(null);

        assertNull(trainee1.getManager());
        assertFalse(topManager.getTrainees().contains(trainee1));
        assertFalse(trainee1.hasManager());
        assertFalse(topManager.isTrainee(trainee1));
    }

    @Test
    void setManagerNullWhenNoManagerDoesNothing() {
        assertNull(trainee1.getManager());

        trainee1.setManager(null);

        assertNull(trainee1.getManager());
        assertFalse(trainee1.hasManager());
    }

    @Test
    void removeTraineeUpdatesBothSides() {
        topManager.addTrainee(trainee1);
        topManager.removeTrainee(trainee1);

        assertNull(trainee1.getManager());
        assertFalse(topManager.getTrainees().contains(trainee1));
        assertEquals(0, topManager.getTraineeCount());
        assertFalse(topManager.hasTrainees());
        assertFalse(topManager.isTrainee(trainee1));
    }

    @Test
    void setSameManagerDoesNothing() {
        trainee1.setManager(topManager);
        int beforeCount = topManager.getTraineeCount();

        trainee1.setManager(topManager);

        assertEquals(beforeCount, topManager.getTraineeCount());
        assertEquals(topManager, trainee1.getManager());
    }

    @Test
    void addTraineeWhenHasDifferentManagerThrowsExceptionAndChangesNothing() {
        trainee1.setManager(topManager);
        assertThrows(IllegalStateException.class,() -> anotherTopManager.addTrainee(trainee1));
        assertEquals(topManager, trainee1.getManager());
        assertTrue(topManager.getTrainees().contains(trainee1));
        assertFalse(anotherTopManager.getTrainees().contains(trainee1));
    }

    @Test
    void masterCannotManageThemselves() {
        assertThrows(IllegalArgumentException.class, () -> topManager.setManager(topManager));
        assertNull(topManager.getManager());
        assertFalse(topManager.hasManager());
    }

    @Test
    void masterCannotBeTheirOwnTrainee() {
        assertThrows(IllegalArgumentException.class, () -> topManager.addTrainee(topManager));
        assertFalse(topManager.hasTrainees());
        assertEquals(0, topManager.getTraineeCount());
    }

    @Test
    void nonTopMasterCannotBeManagerWithSetManager() {
        assertThrows(IllegalStateException.class, () -> trainee1.setManager(nonTop1));
        assertNull(trainee1.getManager());
        assertFalse(nonTop1.getTrainees().contains(trainee1));
        assertFalse(nonTop1.hasTrainees());
    }

    @Test
    void nonTopMasterCannotAddTrainee() {
        assertThrows(IllegalStateException.class, () -> nonTop2.addTrainee(trainee1));
        assertFalse(nonTop2.hasTrainees());
        assertEquals(0, nonTop2.getTraineeCount());
        assertNull(trainee1.getManager());
    }

    @Test
    void addNullTraineeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> topManager.addTrainee(null));
        assertFalse(topManager.hasTrainees());
        assertEquals(0, topManager.getTraineeCount());
    }

    @Test
    void addingSameTraineeTwiceDoesNotDuplicate() {
        topManager.addTrainee(trainee1);
        topManager.addTrainee(trainee1);

        assertEquals(1, topManager.getTraineeCount());
        assertTrue(topManager.getTrainees().contains(trainee1));
        assertEquals(topManager, trainee1.getManager());
    }

    @Test
    void removeTraineeNotInSetDoesNothing() {
        topManager.addTrainee(trainee1);
        topManager.removeTrainee(trainee2);

        assertTrue(topManager.getTrainees().contains(trainee1));
        assertFalse(topManager.getTrainees().contains(trainee2));
        assertEquals(topManager, trainee1.getManager());
        assertNull(trainee2.getManager());
        assertEquals(1, topManager.getTraineeCount());
    }

    @Test
    void removeNullTraineeDoesNothing() {
        topManager.addTrainee(trainee1);
        topManager.removeTrainee(null);

        assertTrue(topManager.getTrainees().contains(trainee1));
        assertEquals(1, topManager.getTraineeCount());
    }

    @Test
    void getTraineesReturnsCopy() {
        topManager.addTrainee(trainee1);

        Set<Master> traineesCopy = topManager.getTrainees();
        traineesCopy.clear();

        assertEquals(1, topManager.getTraineeCount());
        assertTrue(topManager.getTrainees().contains(trainee1));
    }

    @Test
    void hasManagerAndHasTraineesReflectState() {
        assertFalse(trainee1.hasManager());
        assertFalse(topManager.hasTrainees());

        topManager.addTrainee(trainee1);

        assertTrue(trainee1.hasManager());
        assertTrue(topManager.hasTrainees());
    }

    @Test
    void isTraineeReturnsCorrectValue() {
        topManager.addTrainee(trainee1);

        assertTrue(topManager.isTrainee(trainee1));
        assertFalse(topManager.isTrainee(trainee2));
    }

    @Test
    void removeMasterClearsManagerAndTraineeAssociations() {
        Master superManager = Worker.createMaster("Yelizaveta", "Gaiduk", "+48555111228", LocalDate.of(2005, 11, 15), 10).getMaster();

        topManager.setManager(superManager);
        topManager.addTrainee(trainee1);
        topManager.addTrainee(trainee2);

        Master helper = Worker.createMaster("Helper", "Master", "+48555111229", LocalDate.of(1990, 1, 1), 5).getMaster();
        helper.addServiceSpecialisesIn(topManager.getDummyService());

        topManager.removeMaster();

        assertFalse(superManager.getTrainees().contains(topManager));
        assertFalse(superManager.isTrainee(topManager));
        assertNull(trainee1.getManager());
        assertNull(trainee2.getManager());
        assertTrue(topManager.getTrainees().isEmpty());
        assertFalse(topManager.hasTrainees());
        assertFalse(topManager.hasManager());
    }
}
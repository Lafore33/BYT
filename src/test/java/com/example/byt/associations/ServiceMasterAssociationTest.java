package com.example.byt.associations;

import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceMasterAssociationTest {
    private Service service1;
    private Service service2;
    private Master master1;
    private Master master2;

    @BeforeEach
    void setUp() {
        Service.clearExtent();
        Master.clearExtent();

        service1 = new Service(1, "Haircut", 50, "Basic haircut", 1.0);
        service2 = new Service(2, "Hair coloring", 80, "Color your hair", 2.0);

        master1 = new Master("John", "Doe", "123456789", LocalDate.of(1990, 1, 1), 5);
        master2 = new Master("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 20), 7);
    }

    @Test
    void masterConstructorWithServicesAddsAssociations(){
        Master master = new Master("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 20), 7, Set.of(service1, service2));

        assertEquals(1, service1.getMasterSpecializedIn().size());
        assertTrue(service1.getMasterSpecializedIn().contains(master));

        assertEquals(1, service2.getMasterSpecializedIn().size());
        assertTrue(service2.getMasterSpecializedIn().contains(master));

        assertEquals(2, master.getServiceSpecialisesIn().size());
        assertTrue(master.getServiceSpecialisesIn().contains(service1));
        assertTrue(master.getServiceSpecialisesIn().contains(service2));
    }

    @Test
    void masterConstructorWithNullServicesShouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> new Master("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 20), 7, null));
    }

    @Test
    void masterConstructorWithEmptyServicesShouldThrowException(){
        assertThrows(IllegalArgumentException.class, () -> new Master("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 20), 7, Set.of()));
    }

    @Test
    void masterConstructorWithSetContainingNullServiceShouldThrowsException(){
        Set<Service> services = new HashSet<>();
        services.add(null);
        services.add(service1);
        assertThrows(IllegalArgumentException.class, () -> new Master("Jane", "Smith", "987654321", LocalDate.of(1985, 5, 20), 7, services));
    }

    @Test
    void addServiceSpecialisesInUpdatesBothSides(){
        master1.addServiceSpecialisesIn(service1);

        assertEquals(1, service1.getMasterSpecializedIn().size());
        assertTrue(service1.getMasterSpecializedIn().contains(master1));

        assertEquals(1, master1.getServiceSpecialisesIn().size());
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void addMasterSpecialisedInUpdatesBothSides(){
        service1.addMasterSpecializedIn(master1);

        assertEquals(1, service1.getMasterSpecializedIn().size());
        assertTrue(service1.getMasterSpecializedIn().contains(master1));

        assertEquals(1, master1.getServiceSpecialisesIn().size());
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void addNullServiceSpecialisesInThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> master1.addServiceSpecialisesIn(null));

        assertTrue(master1.getServiceSpecialisesIn().isEmpty());
    }

    @Test
    void addNullMasterSpecialisedInThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> service1.addMasterSpecializedIn(null));

        assertTrue(service1.getMasterSpecializedIn().isEmpty());
    }

    @Test
    void addDuplicateServiceSpecialisesInDoesNotDuplicate(){
        master1.addServiceSpecialisesIn(service1);
        master1.addServiceSpecialisesIn(service1);

        assertEquals(1, service1.getMasterSpecializedIn().size());
        assertEquals(1, master1.getServiceSpecialisesIn().size());
    }

    @Test
    void removeServiceSpecialisesUpdatesBothSides(){
        //avoid exception in services
        service1.addMasterSpecializedIn(master2);
        service2.addMasterSpecializedIn(master2);

        master1.addServiceSpecialisesIn(service1);
        master1.addServiceSpecialisesIn(service2);

        master1.removeServiceSpecialisesIn(service2);
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertFalse(master1.getServiceSpecialisesIn().contains(service2));

        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertFalse(service2.getMasterSpecializedIn().contains(master1));

    }

    @Test
    void removeMasterSpecialisedInUpdatesBothSides(){
        //avoid exception in masters
        master1.addServiceSpecialisesIn(service2);
        master2.addServiceSpecialisesIn(service2);

        service1.addMasterSpecializedIn(master1);
        service1.addMasterSpecializedIn(master2);

        service1.removeMasterSpecializedIn(master2);

        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertFalse(service1.getMasterSpecializedIn().contains(master2));

        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertFalse(master2.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void removeNullServiceSpecialisesInDoesNothing(){
        master1.addServiceSpecialisesIn(service1);
        master1.removeServiceSpecialisesIn(null);
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertEquals(1, master1.getServiceSpecialisesIn().size());
    }

    @Test
    void removeNullMasterSpecialisedInDoesNothing(){
        service1.addMasterSpecializedIn(master1);
        service1.removeMasterSpecializedIn(null);
        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertEquals(1, service1.getMasterSpecializedIn().size());
    }

    @Test
    void removeAllServicesSpecialisesInThrowsExceptionAndChangesNothing(){
        //avoid exceptions in services
        service1.addMasterSpecializedIn(master2);
        service2.addMasterSpecializedIn(master2);

        master1.addServiceSpecialisesIn(service1);
        master1.addServiceSpecialisesIn(service2);

        master1.removeServiceSpecialisesIn(service2);
        assertThrows(IllegalStateException.class, () -> master1.removeServiceSpecialisesIn(service1));

        assertTrue(master1.getServiceSpecialisesIn().contains(service1), "Expected to have in servicesSpecialisesIn service1");
        assertTrue(service1.getMasterSpecializedIn().contains(master1), "Expected to have in masterSpecializedIn master1");
    }

    @Test
    void removeAllMasterSpecialisedInThrowsExceptionAndChangesNothing(){
        //avoid exception in masters
        master1.addServiceSpecialisesIn(service2);
        master2.addServiceSpecialisesIn(service2);

        service1.addMasterSpecializedIn(master1);
        service1.addMasterSpecializedIn(master2);

        service1.removeMasterSpecializedIn(master2);
        assertThrows(IllegalStateException.class, () -> service1.removeMasterSpecializedIn(master1));

        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void removeServiceWhichHasOnlyThisMasterSpecializedInThrowsExceptionAndChangesNothing(){
        master1.addServiceSpecialisesIn(service1);
        master1.addServiceSpecialisesIn(service2);

        assertThrows(IllegalStateException.class, () -> master1.removeServiceSpecialisesIn(service2));
        assertEquals(2, master1.getServiceSpecialisesIn().size());
        assertTrue(master1.getServiceSpecialisesIn().contains(service2));
        assertTrue(service2.getMasterSpecializedIn().contains(master1));
    }

    @Test
    void removeMasterWhichHasOnlyThisServiceSpecializesInThrowsExceptionAndChangesNothing(){
        service1.addMasterSpecializedIn(master1);
        service1.addMasterSpecializedIn(master2);

        assertThrows(IllegalStateException.class, () -> service1.removeMasterSpecializedIn(master2));
        assertEquals(2, service1.getMasterSpecializedIn().size());
        assertTrue(service1.getMasterSpecializedIn().contains(master2));
        assertTrue(master2.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void removeServiceThatIsNotInSetDoesNothing(){
        //to avoid exception in service
        service2.addMasterSpecializedIn(master2);

        master1.addServiceSpecialisesIn(service1);
        master1.removeServiceSpecialisesIn(service2);

        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertFalse(master1.getServiceSpecialisesIn().contains(service2));
        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertFalse(service2.getMasterSpecializedIn().contains(master1));
        assertEquals(1, master1.getServiceSpecialisesIn().size());
    }

    @Test
    void removeMasterThatIsNotInSetDoesNothing(){
        //to avoid exception in master
        master2.addServiceSpecialisesIn(service2);

        service1.addMasterSpecializedIn(master1);
        service1.removeMasterSpecializedIn(master2);

        assertTrue(service1.getMasterSpecializedIn().contains(master1));
        assertFalse(service1.getMasterSpecializedIn().contains(master2));
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertFalse(master2.getServiceSpecialisesIn().contains(service1));
        assertEquals(1, service1.getMasterSpecializedIn().size());

    }

    @Test
    void getServiceSpecialisesInReturnsCopy(){
        master1.addServiceSpecialisesIn(service1);
        Set<Service> services = master1.getServiceSpecialisesIn();
        services.clear();
        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
    }

    @Test
    void getMasterSpecializedInReturnsCopy(){
        service1.addMasterSpecializedIn(master1);
        Set<Master> masters = service1.getMasterSpecializedIn();
        masters.clear();
        assertTrue(service1.getMasterSpecializedIn().contains(master1));
    }

    @Test
    void removeServiceClearsAllAssociations(){
        //t avoid exception in master
        master1.addServiceSpecialisesIn(service2);
        master2.addServiceSpecialisesIn(service2);

        service1.addMasterSpecializedIn(master1);
        service1.addMasterSpecializedIn(master2);

        service1.removeService();

        assertFalse(master1.getServiceSpecialisesIn().contains(service1));
        assertFalse(master2.getServiceSpecialisesIn().contains(service1));

        assertTrue(service1.getMasterSpecializedIn().isEmpty());
    }

    @Test
    void removeMasterClearsAllAssociations(){
        //to avoid exceptions in service
        service1.addMasterSpecializedIn(master2);
        service2.addMasterSpecializedIn(master2);

        master1.addServiceSpecialisesIn(service1);
        master1.addServiceSpecialisesIn(service2);

        master1.removeMaster();

        assertFalse(service1.getMasterSpecializedIn().contains(master1));
        assertFalse(service2.getMasterSpecializedIn().contains(master1));
        assertTrue(master1.getServiceSpecialisesIn().isEmpty());

    }

    @Test
    void removeServiceWhenMasterHasOnlyThisServiceSpecializesInThrowsExceptionAndChangesNothing(){

        service1.addMasterSpecializedIn(master1);

        assertThrows(IllegalStateException.class, () -> service1.removeService());

        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertTrue(service1.getMasterSpecializedIn().contains(master1));
    }

    @Test
    void removeMasterWhenServiceHasOnlyThisServiceSpecializesInThrowsExceptionAndChangesNothing(){
        master1.addServiceSpecialisesIn(service1);

        assertThrows(IllegalStateException.class, () -> master1.removeMaster());

        assertTrue(master1.getServiceSpecialisesIn().contains(service1));
        assertTrue(service1.getMasterSpecializedIn().contains(master1));
    }


}

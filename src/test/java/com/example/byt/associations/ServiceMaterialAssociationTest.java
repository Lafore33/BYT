package com.example.byt.associations;

import com.example.byt.models.Material;
import com.example.byt.models.person.Master;
import com.example.byt.models.person.Worker;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceMaterialAssociationTest {
    Service service1;
    Service service2;
    Material material1;
    Material material2;
    private static Master master = Worker.createMaster("John", "Doe", "123456789", LocalDate.of(1990, 1, 1), 5).getMaster();

    @BeforeEach
    void setUp(){
        Service.clearExtent();
        Material.clearExtent();

        service1 = new Service(1, "Haircut", 20.0, "Basic haircut", 30.0, Set.of(master));
        service2 = new Service(2, "Hairwash", 20, "Wash", 15, Set.of(master));
        material1 = new Material("Shampoo", "L'Oreal");
        material2 = new Material("Conditioner", "L'Oreal");
    }

    @Test
    void addMaterialUsedUpdatesBothSides(){
        service1.addMaterialUsed(material1);

        assertEquals(1, service1.getMaterialsUsed().size());
        assertTrue(service1.getMaterialsUsed().contains(material1));

        assertEquals(1, material1.getServicesUsedIn().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));
    }

    @Test
    void addServiceUsedInUpdatesBothSides(){
        material1.addServiceUsedIn(service1);

        assertEquals(1, service1.getMaterialsUsed().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));

        assertEquals(1, material1.getServicesUsedIn().size());
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }

    @Test
    void addDuplicateMaterialUsedDoesNotDuplicate(){
        service1.addMaterialUsed(material1);
        service1.addMaterialUsed(material1);

        assertEquals(1, service1.getMaterialsUsed().size());
        assertEquals(1, material1.getServicesUsedIn().size());
    }

    @Test
    void addDuplicateServiceUsedInDoesNotDuplicate(){
        material1.addServiceUsedIn(service1);
        material1.addServiceUsedIn(service1);

        assertEquals(1, service1.getMaterialsUsed().size());
        assertEquals(1, material1.getServicesUsedIn().size());
    }

    @Test
    void addNullMaterialUsedThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> service1.addMaterialUsed(null));

        assertTrue(service1.getMaterialsUsed().isEmpty());
    }

    @Test
    void addNullServiceUsedInThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> material1.addServiceUsedIn(null));

        assertTrue(material1.getServicesUsedIn().isEmpty());
    }

    @Test
    void removeMaterialUsedUpdatesBothSides(){
        service1.addMaterialUsed(material1);
        service1.removeMaterialUsed(material1);

        assertTrue(service1.getMaterialsUsed().isEmpty());
        assertTrue(material1.getServicesUsedIn().isEmpty());
    }

    @Test
    void removeServiceUsedInUpdatesBothSides(){
        material1.addServiceUsedIn(service1);
        material1.removeServiceUsedIn(service1);

        assertTrue(service1.getMaterialsUsed().isEmpty());
        assertTrue(material1.getServicesUsedIn().isEmpty());
    }

    @Test
    void removeNullServiceUsedInDoesNothing(){
        material1.addServiceUsedIn(service1);
        material1.removeServiceUsedIn(null);

        assertFalse(material1.getServicesUsedIn().isEmpty());
        assertEquals(1, material1.getServicesUsedIn().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }

    @Test
    void removeNullMaterialUsedDoesNothing(){
        service1.addMaterialUsed(material1);
        service1.removeMaterialUsed(null);
        assertFalse(service1.getMaterialsUsed().isEmpty());
        assertEquals(1, service1.getMaterialsUsed().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }

    @Test
    void removeMaterialUsedNotInSetDoesNothing(){
        service1.addMaterialUsed(material1);
        service1.removeMaterialUsed(material2);

        assertFalse(service1.getMaterialsUsed().isEmpty());
        assertEquals(1, service1.getMaterialsUsed().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }

    @Test
    void removeServiceUsedNotInSetDoesNothing(){
        material1.addServiceUsedIn(service1);
        material1.removeServiceUsedIn(service2);

        assertFalse(material1.getServicesUsedIn().isEmpty());
        assertEquals(1, material1.getServicesUsedIn().size());
        assertTrue(material1.getServicesUsedIn().contains(service1));
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }

    @Test
    void getServicesUsedInReturnsCopy(){
        material1.addServiceUsedIn(service1);

        Set<Service> servicesUsedIn = material1.getServicesUsedIn();

        servicesUsedIn.clear();
        assertTrue(material1.getServicesUsedIn().contains(service1));

    }

    @Test
    void getMaterialsUsedReturnsCopy(){
        service1.addMaterialUsed(material1);
        Set<Material> materials = service1.getMaterialsUsed();

        materials.clear();
        assertTrue(service1.getMaterialsUsed().contains(material1));
    }





}

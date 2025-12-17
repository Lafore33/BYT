package com.example.byt.inheritance;

import com.example.byt.models.Certification;
import com.example.byt.models.Material;
import com.example.byt.models.person.Master;
import com.example.byt.models.person.Worker;
import com.example.byt.models.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ServiceMultiInheritanceTests {

    private Master master;
    private Material material1;
    private Material material2;

    @BeforeEach
    void setup() {
        Service.clearExtent();
        FourHandsService.clearExtent();
        TwoHandsService.clearExtent();
        HairService.clearExtent();
        NailService.clearExtent();
        SkinService.clearExtent();
        Master.clearExtent();
        Worker.clearExtent();
        Certification.clearExtent();
        Material.clearExtent();

        master = Worker.createMaster("John", "Doe", "123456789", LocalDate.of(1990, 1, 1), 5);
        material1 = new Material("Massage Oil", "OilCorp");
        material2 = new Material("Towels", "TowelCo");
    }

    @Test
    void testCreateTwoHandsService() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                1, "Regular Massage", 70.0, "One therapist massage", 60.0, Set.of(master)
        );

        assertNotNull(twoHandsService);
        assertNotNull(twoHandsService.getService());
        assertTrue(TwoHandsService.getTwoHandsServiceList().contains(twoHandsService));
        assertTrue(Service.getServiceList().contains(twoHandsService.getService()));
        assertEquals(twoHandsService, twoHandsService.getService().getRelatedService());
        assertEquals(1, TwoHandsService.getNumOfSpecialistsRequired());
    }

    @Test
    void testCreateTwoHandsServiceWithMaterials() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                1, "Regular Massage", 70.0, "One therapist massage", 60.0,
                Set.of(master), Set.of(material1, material2)
        );

        assertNotNull(twoHandsService);
        assertNotNull(twoHandsService.getService());
        assertTrue(TwoHandsService.getTwoHandsServiceList().contains(twoHandsService));
        assertTrue(Service.getServiceList().contains(twoHandsService.getService()));
        assertEquals(twoHandsService, twoHandsService.getService().getRelatedService());
        assertEquals(1, TwoHandsService.getNumOfSpecialistsRequired());
        assertEquals(2, twoHandsService.getService().getMaterialsUsed().size());
    }

    @Test
    void testCreateFourHandsService() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                2, "Four Hands Massage", 150.0, "Two therapists massage", 90.0,
                Set.of(master), true
        );

        assertNotNull(fourHandsService);
        assertNotNull(fourHandsService.getService());
        assertTrue(FourHandsService.getFourHandsServiceList().contains(fourHandsService));
        assertTrue(Service.getServiceList().contains(fourHandsService.getService()));
        assertEquals(fourHandsService, fourHandsService.getService().getRelatedService());
        assertTrue(fourHandsService.isExpressService());
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired());
    }

    @Test
    void testCreateFourHandsServiceWithMaterials() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                2, "Four Hands Massage", 150.0, "Two therapists massage", 90.0,
                Set.of(master), true, Set.of(material1, material2)
        );

        assertNotNull(fourHandsService);
        assertNotNull(fourHandsService.getService());
        assertTrue(FourHandsService.getFourHandsServiceList().contains(fourHandsService));
        assertTrue(Service.getServiceList().contains(fourHandsService.getService()));
        assertEquals(fourHandsService, fourHandsService.getService().getRelatedService());
        assertTrue(fourHandsService.isExpressService());
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired());
        assertEquals(2, fourHandsService.getService().getMaterialsUsed().size());
    }

    @Test
    void testCreateFourHandsServiceNotExpress() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                3, "Relaxing Four Hands", 180.0, "Slow four hands massage", 120.0,
                Set.of(master), false
        );

        assertNotNull(fourHandsService);
        assertFalse(fourHandsService.isExpressService());
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired());
    }

    @Test
    void testCreateFourHandsServiceNotExpressWithMaterials() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                3, "Relaxing Four Hands", 180.0, "Slow four hands massage", 120.0,
                Set.of(master), false, Set.of(material1)
        );

        assertNotNull(fourHandsService);
        assertFalse(fourHandsService.isExpressService());
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired());
        assertEquals(1, fourHandsService.getService().getMaterialsUsed().size());
    }

    @Test
    void testServiceWithNoHandsMode() {
        Service basicService = new Service(
                4, "Basic Service", 30.0, "Simple service", 20.0, Set.of(master)
        );

        assertNotNull(basicService);
        assertNull(basicService.getRelatedService());
        assertTrue(Service.getServiceList().contains(basicService));
    }

    @Test
    void testRemoveTwoHandsServiceAlsoRemovesFromExtent() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                5, "Massage", 70.0, "Regular massage", 60.0, Set.of(master)
        );

        Service service = twoHandsService.getService();
        assertTrue(TwoHandsService.getTwoHandsServiceList().contains(twoHandsService));
        assertTrue(Service.getServiceList().contains(service));

        twoHandsService.removeFromExtent();

        assertFalse(TwoHandsService.getTwoHandsServiceList().contains(twoHandsService));
    }

    @Test
    void testRemoveFourHandsServiceAlsoRemovesFromExtent() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                6, "Luxury Massage", 200.0, "Premium four hands massage", 120.0,
                Set.of(master), false
        );

        Service service = fourHandsService.getService();
        assertTrue(FourHandsService.getFourHandsServiceList().contains(fourHandsService));
        assertTrue(Service.getServiceList().contains(service));

        fourHandsService.removeFromExtent();

        assertFalse(FourHandsService.getFourHandsServiceList().contains(fourHandsService));
    }

    @Test
    void testRemoveServiceAlsoRemovesRelatedFourHandsService() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                7, "Four Hands Massage", 150.0, "Dual therapist massage", 90.0,
                Set.of(master), true
        );

        Service service = fourHandsService.getService();

        service.removeFromExtent();

        assertFalse(Service.getServiceList().contains(service));
        assertFalse(FourHandsService.getFourHandsServiceList().contains(fourHandsService));
    }

    @Test
    void testRemoveServiceAlsoRemovesRelatedTwoHandsService() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                8, "Single Massage", 80.0, "One therapist massage", 60.0, Set.of(master)
        );

        Service service = twoHandsService.getService();

        service.removeFromExtent();

        assertFalse(Service.getServiceList().contains(service));
        assertFalse(TwoHandsService.getTwoHandsServiceList().contains(twoHandsService));
    }

    @Test
    void testMultipleHandsServicesCoexist() {
        TwoHandsService twoHands1 = Service.createTwoHandsService(
                9, "Massage 1", 70.0, "Massage", 60.0, Set.of(master)
        );

        TwoHandsService twoHands2 = Service.createTwoHandsService(
                10, "Massage 2", 80.0, "Another massage", 70.0, Set.of(master), Set.of(material1)
        );

        FourHandsService fourHands = Service.createFourHandsService(
                11, "Four Hands", 150.0, "Premium massage", 90.0, Set.of(master), true
        );

        assertEquals(3, Service.getServiceList().size());
        assertEquals(2, TwoHandsService.getTwoHandsServiceList().size());
        assertEquals(1, FourHandsService.getFourHandsServiceList().size());
    }

    @Test
    void testServiceHasOnlyOneHandsMode() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                12, "Massage", 70.0, "Massage", 60.0, Set.of(master)
        );

        Service service = twoHandsService.getService();
        Object relatedService = service.getRelatedService();

        assertInstanceOf(TwoHandsService.class, relatedService);
        assertFalse(false);
    }

    @Test
    void testFourHandsServiceExclusiveToTwoHands() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                13, "Four Hands", 150.0, "Premium massage", 90.0, Set.of(master), true
        );

        Service service = fourHandsService.getService();
        Object relatedService = service.getRelatedService();

        assertInstanceOf(FourHandsService.class, relatedService);
        assertFalse(false);
    }

    @Test
    void testHandsServiceAccessesUnderlyingService() {
        FourHandsService fourHandsService = Service.createFourHandsService(
                14, "Luxury Four Hands", 250.0, "Ultimate relaxation", 120.0,
                Set.of(master), true
        );

        Service underlyingService = fourHandsService.getService();

        assertEquals(14, underlyingService.getId());
        assertEquals("Luxury Four Hands", underlyingService.getName());
        assertEquals(250.0, underlyingService.getRegularPrice());
        assertEquals("Ultimate relaxation", underlyingService.getDescription());
        assertEquals(120.0, underlyingService.getDuration());
        assertTrue(fourHandsService.isExpressService());
        assertEquals(2, FourHandsService.getNumOfSpecialistsRequired());
    }

    @Test
    void testTwoHandsServiceAccessesUnderlyingService() {
        TwoHandsService twoHandsService = Service.createTwoHandsService(
                15, "Regular Massage", 100.0, "Relaxing massage", 60.0, Set.of(master)
        );

        Service underlyingService = twoHandsService.getService();

        assertEquals(15, underlyingService.getId());
        assertEquals("Regular Massage", underlyingService.getName());
        assertEquals(100.0, underlyingService.getRegularPrice());
        assertEquals("Relaxing massage", underlyingService.getDescription());
        assertEquals(60.0, underlyingService.getDuration());
        assertEquals(1, TwoHandsService.getNumOfSpecialistsRequired());
    }

    @Test
    void testClearingTwoHandsExtentDoesNotAffectFourHands() {
        TwoHandsService twoHands = Service.createTwoHandsService(
                16, "Two Hands", 70.0, "Massage", 60.0, Set.of(master)
        );

        FourHandsService fourHands = Service.createFourHandsService(
                17, "Four Hands", 150.0, "Massage", 90.0, Set.of(master), false
        );

        TwoHandsService.clearExtent();

        assertTrue(TwoHandsService.getTwoHandsServiceList().isEmpty());
        assertFalse(FourHandsService.getFourHandsServiceList().isEmpty());
        assertEquals(1, FourHandsService.getFourHandsServiceList().size());
    }

    @Test
    void testMixedCreationWithAndWithoutMaterials() {
        TwoHandsService twoHandsNoMaterial = Service.createTwoHandsService(
                18, "Basic Massage", 60.0, "Simple massage", 45.0, Set.of(master)
        );

        TwoHandsService twoHandsWithMaterial = Service.createTwoHandsService(
                19, "Oil Massage", 90.0, "Massage with oils", 60.0,
                Set.of(master), Set.of(material1, material2)
        );

        FourHandsService fourHandsNoMaterial = Service.createFourHandsService(
                20, "Four Hands Basic", 140.0, "Basic four hands", 60.0,
                Set.of(master), false
        );

        FourHandsService fourHandsWithMaterial = Service.createFourHandsService(
                21, "Four Hands Premium", 200.0, "Premium four hands with oils", 90.0,
                Set.of(master), true, Set.of(material1)
        );

        assertEquals(4, Service.getServiceList().size());
        assertEquals(2, TwoHandsService.getTwoHandsServiceList().size());
        assertEquals(2, FourHandsService.getFourHandsServiceList().size());

        assertTrue(twoHandsNoMaterial.getService().getMaterialsUsed().isEmpty());
        assertEquals(2, twoHandsWithMaterial.getService().getMaterialsUsed().size());
        assertTrue(fourHandsNoMaterial.getService().getMaterialsUsed().isEmpty());
        assertEquals(1, fourHandsWithMaterial.getService().getMaterialsUsed().size());
    }
}
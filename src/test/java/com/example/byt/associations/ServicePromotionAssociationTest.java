package com.example.byt.associations;

import com.example.byt.models.Promotion;
import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServicePromotionAssociationTest {
    Service service1;
    Service service2;
    Promotion promotion1;
    Promotion promotion2;
    private static Master master = new Master("John", "Doe", "123456789", LocalDate.of(1990, 1, 1), 5);

    @BeforeEach
    public void setUp() {
        Promotion.clearExtent();
        Service.clearExtent();

        service1 = new Service(1, "Haircut", 100, "Basic", 30, Set.of(master));
        service2 = new Service(2, "Hairwash", 100, "Wash", 15, Set.of(master));

        promotion1 = new Promotion(
                "Winter", "Winter sale", 20,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                Set.of(service1)
        );

        promotion2 = new Promotion(
                "Birthday", "Birthday sale", 10,
                LocalDate.now().minusDays(25),
                LocalDate.now().plusDays(25),
                Set.of(service1)
        );
    }

    @Test
    void addPromotionAppliedUpdatesBothSides(){
        service2.addPromotionApplied(promotion1);

        assertTrue(service2.getPromotionsApplied().contains(promotion1));

        assertTrue(promotion1.getServicesApplicableTo().contains(service2));
    }

    @Test
    void addServiceApplicableToUpdatesBothSides(){
        promotion1.addServiceApplicableTo(service2);

        assertTrue(service2.getPromotionsApplied().contains(promotion1));

        assertTrue(promotion1.getServicesApplicableTo().contains(service2));
    }

    @Test
    void addNullPromotionAppliedThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> service1.addPromotionApplied(null));
    }

    @Test
    void addNullServiceApplicableToThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> promotion1.addServiceApplicableTo(null));
    }

    @Test
    void addDuplicatePromotionAppliedDoesNothing(){
        service1.addPromotionApplied(promotion1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(2, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void addDuplicateServiceApplicableToDoesNothing(){
        promotion1.addServiceApplicableTo(service1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(2, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void removePromotionAppliedUpdatesBothSides(){
        promotion1.addServiceApplicableTo(service2);

        service1.removePromotionApplied(promotion1);

        assertFalse(service1.getPromotionsApplied().contains(promotion1));

        assertFalse(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());

    }

    @Test
    void removeServiceApplicableToUpdatesBothSides(){
        promotion1.addServiceApplicableTo(service2);

        promotion1.removeServiceApplicableTo(service1);

        assertFalse(service1.getPromotionsApplied().contains(promotion1));

        assertFalse(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());

    }

    @Test
    void removeNullPromotionAppliedDoesNothing(){
        service1.removePromotionApplied(null);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removeNullServiceApplicableToDoesNothing(){
        promotion1.removeServiceApplicableTo(null);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removePromotionAppliedNotInSetDoesNothing(){
        service2.removePromotionApplied(promotion2);

        assertFalse(service2.getPromotionsApplied().contains(promotion2));
        assertFalse(promotion2.getServicesApplicableTo().contains(service2));
    }

    @Test
    void removeServiceApplicableToNotInSetDoesNothing(){
        promotion1.removeServiceApplicableTo(service2);


        assertFalse(service2.getPromotionsApplied().contains(promotion1));
        assertFalse(promotion1.getServicesApplicableTo().contains(service2));
    }

    @Test
    void removeAllServicesApplicableToThrowsExceptionAndChangesNothing(){
        promotion1.addServiceApplicableTo(service2);

        promotion1.removeServiceApplicableTo(service2);
        assertThrows(IllegalStateException.class, () -> promotion1.removeServiceApplicableTo(service1));

        assertEquals(1, promotion1.getServicesApplicableTo().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removePromotionAppliedThatHasOnlyThisServiceThrowsExceptionAndChangesNothing(){
        assertThrows(IllegalStateException.class, () -> service1.removePromotionApplied(promotion1));

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }


    @Test
    void getPromotionAppliedReturnsCopy(){
        Set<Promotion> promotions = service1.getPromotionsApplied();
        promotions.clear();
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
    }

    @Test
    void getServicesApplicableToReturnsCopy(){
        Set<Service> services = promotion1.getServicesApplicableTo();
        services.clear();
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void getTotalPriceReturnsRegularPriceWhenNoPromotions(){
        assertEquals(100, service2.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsRegularPriceWhenPromotionsOutdated(){
        Promotion prom = new Promotion("Promotion", "Promotion", 20, LocalDate.now().minusDays(100), LocalDate.now().minusDays(25), Set.of(service2));
        assertEquals(100, service2.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsCalculatedPriceBasedOnPromotionsApplied(){
        service2.addPromotionApplied(promotion1);
        assertEquals(80, service2.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsCalculatedPriceBasedOnBiggestPromotionApplied(){
        assertEquals(80, service1.getTotalPrice());
    }


}

package com.example.byt.associations;

import com.example.byt.models.Promotion;
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

    @BeforeEach
    public void setUp() {
        Promotion.clearExtent();
        Service.clearExtent();

        service1 = new Service(1, "Haircut", 100, "Basic", 30);
        service2 = new Service(2, "Hairwash", 20, "Wash", 15);

        promotion1 = new Promotion(
                "Winter", "Winter sale", 20,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        promotion2 = new Promotion(
                "Birthday", "Birthday sale", 10,
                LocalDate.now().minusDays(25),
                LocalDate.now().plusDays(25)
        );
    }

    @Test
    void addPromotionAppliedUpdatesBothSides(){
        service1.addPromotionApplied(promotion1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(1, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void addServiceApplicableToUpdatesBothSides(){
        promotion1.addServiceApplicableTo(service1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(1, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void addNullPromotionAppliedThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> service1.addPromotionApplied(null));
        assertTrue(service1.getPromotionsApplied().isEmpty());
    }

    @Test
    void addNullServiceApplicableToThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> promotion1.addServiceApplicableTo(null));
        assertTrue(promotion1.getServicesApplicableTo().isEmpty());
    }

    @Test
    void addDuplicatePromotionAppliedDoesNothing(){
        service1.addPromotionApplied(promotion1);
        service1.addPromotionApplied(promotion1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(1, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void addDuplicateServiceApplicableToDoesNothing(){
        promotion1.addServiceApplicableTo(service1);
        promotion1.addServiceApplicableTo(service1);

        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertEquals(1, service1.getPromotionsApplied().size());

        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());
    }

    @Test
    void removePromotionAppliedUpdatesBothSides(){
        //to avoid exception in promotion
        promotion1.addServiceApplicableTo(service2);

        service1.addPromotionApplied(promotion1);
        service1.removePromotionApplied(promotion1);

        assertFalse(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(service1.getPromotionsApplied().isEmpty());

        assertFalse(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());

    }

    @Test
    void removeServiceApplicableToUpdatesBothSides(){
        //to avoid exception in promotion
        promotion1.addServiceApplicableTo(service2);

        promotion1.addServiceApplicableTo(service1);
        promotion1.removeServiceApplicableTo(service1);

        assertFalse(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(service1.getPromotionsApplied().isEmpty());

        assertFalse(promotion1.getServicesApplicableTo().contains(service1));
        assertEquals(1, promotion1.getServicesApplicableTo().size());

    }

    @Test
    void removeNullPromotionAppliedDoesNothing(){
        service1.addPromotionApplied(promotion1);
        service1.removePromotionApplied(null);

        assertEquals(1, service1.getPromotionsApplied().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removeNullServiceApplicableToDoesNothing(){
        promotion1.addServiceApplicableTo(service1);
        promotion1.removeServiceApplicableTo(null);

        assertEquals(1, promotion1.getServicesApplicableTo().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removePromotionAppliedNotInSetDoesNothing(){
        service1.addPromotionApplied(promotion1);
        service1.removePromotionApplied(promotion2);

        assertEquals(1, service1.getPromotionsApplied().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertFalse(service1.getPromotionsApplied().contains(promotion2));
        assertFalse(promotion2.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removeServiceApplicableToNotInSetDoesNothing(){
        promotion1.addServiceApplicableTo(service1);
        promotion1.removeServiceApplicableTo(service2);

        assertEquals(1, promotion1.getServicesApplicableTo().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));

        assertFalse(service1.getPromotionsApplied().contains(promotion2));
        assertFalse(promotion2.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removeAllServicesApplicableToThrowsExceptionAndChangesNothing(){
        promotion1.addServiceApplicableTo(service1);
        promotion1.addServiceApplicableTo(service2);

        promotion1.removeServiceApplicableTo(service2);
        assertThrows(IllegalStateException.class, () -> promotion1.removeServiceApplicableTo(service1));

        assertEquals(1, promotion1.getServicesApplicableTo().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removePromotionAppliedThatHasOnlyThisServiceThrowsExceptionAndChangesNothing(){
        service1.addPromotionApplied(promotion1);
        assertThrows(IllegalStateException.class, () -> service1.removePromotionApplied(promotion1));

        assertEquals(1, service1.getPromotionsApplied().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removePromotionClearsAllAssociations(){
        promotion1.addServiceApplicableTo(service1);
        promotion1.addServiceApplicableTo(service2);

        promotion1.removePromotion();

        assertTrue(promotion1.getServicesApplicableTo().isEmpty());
        assertTrue(service1.getPromotionsApplied().isEmpty());
        assertTrue(service2.getPromotionsApplied().isEmpty());

    }

    @Test
    void removeServiceClearsAllAssociations(){
        //to avoid exception
        promotion1.addServiceApplicableTo(service2);
        promotion2.addServiceApplicableTo(service2);

        service1.addPromotionApplied(promotion1);
        service1.addPromotionApplied(promotion2);

        service1.removeService();

        assertTrue(service1.getPromotionsApplied().isEmpty());
        assertFalse(promotion1.getServicesApplicableTo().contains(service1));
        assertFalse(promotion2.getServicesApplicableTo().contains(service1));
    }

    @Test
    void removeServiceWhenPromotionHasOnlyThisServiceThrowsExceptionAndChangesNothing(){
        service1.addPromotionApplied(promotion1);

        assertThrows(IllegalStateException.class, () -> service1.removeService());
        assertEquals(1, service1.getPromotionsApplied().size());
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
        assertTrue(Service.getServiceList().contains(service1));
    }

    @Test
    void getPromotionAppliedReturnsCopy(){
        service1.addPromotionApplied(promotion1);
        Set<Promotion> promotions = service1.getPromotionsApplied();
        promotions.clear();
        assertTrue(service1.getPromotionsApplied().contains(promotion1));
    }

    @Test
    void getServicesApplicableToReturnsCopy(){
        promotion1.addServiceApplicableTo(service1);
        Set<Service> services = promotion1.getServicesApplicableTo();
        services.clear();
        assertTrue(promotion1.getServicesApplicableTo().contains(service1));
    }

    @Test
    void getTotalPriceReturnsRegularPriceWhenNoPromotions(){
        assertEquals(100, service1.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsRegularPriceWhenPromotionsOutdated(){
        Promotion prom = new Promotion("Promotion", "Promotion", 20, LocalDate.now().minusDays(100), LocalDate.now().minusDays(25));
        service1.addPromotionApplied(prom);
        assertEquals(100, service1.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsCalculatedPriceBasedOnPromotionsApplied(){
        service1.addPromotionApplied(promotion1);
        assertEquals(80, service1.getTotalPrice());
    }

    @Test
    void getTotalPriceReturnsCalculatedPriceBasedOnBiggestPromotionApplied(){
        service1.addPromotionApplied(promotion1);
        service1.addPromotionApplied(promotion2);
        assertEquals(80, service1.getTotalPrice());
    }


}

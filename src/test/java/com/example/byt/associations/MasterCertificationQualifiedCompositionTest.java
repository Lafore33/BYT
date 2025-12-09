package com.example.byt.associations;

import com.example.byt.models.Certification;
import com.example.byt.models.person.Master;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

public class MasterCertificationQualifiedCompositionTest {

    private Master master1;
    private Master master2;

    @BeforeEach
    void setUp() {
        Master.clearExtent();
        Certification.clearExtent();

        master1 = new Master("Yelizaveta", "Gaiduk", "+48555111222", LocalDate.of(2005, 11, 15), 5);
        master2 = new Master("Dana", "Nazarchuk", "+48550111222", LocalDate.of(1990, 1, 1), 10);
    }

    @Test
    void certificationConstructorWithMasterCreatesQualifiedAndComposition() {
        Certification cert = new Certification(master1, "Brow Lamination", "CERT-001", "Basic course", "NoOffence Academy", LocalDate.now());

        assertSame(cert, master1.getCertificationByNumber("CERT-001"));
        assertSame(master1, cert.getMaster());
        assertTrue(Certification.getCertificationList().contains(cert));
    }

    @Test
    void constructorAddsDummyCertificationAndRemovesItOnRealAdd() {
        assertTrue(master1.existsDummyCertification());
        Certification cert = new Certification(master1, "Lamination", "CERT-002", "Advanced", "NoOffence", LocalDate.now());

        assertFalse(master1.existsDummyCertification());
        assertSame(cert, master1.getCertificationByNumber("CERT-002"));
    }

    @Test
    void constructorWithNullMasterThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Certification(null, "Name", "X1", "Desc", "Org", LocalDate.now()));
    }

    @Test
    void addCertificationUpdatesBothSides() {
        Certification cert = new Certification(master1, "Course", "CERT-003", "Desc", "Academy", LocalDate.now());

        assertSame(master1, cert.getMaster());
        assertSame(cert, master1.getCertificationByNumber("CERT-003"));
    }

    @Test
    void addNullCertificationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> master1.addCertification(null));
    }

    @Test
    void addCertificationWithDuplicateNumberButDifferentInstanceThrows() {
        Certification cert1 = new Certification(master1, "CourseA", "CERT-005", "Desc", "Org", LocalDate.now());
        Certification cert2 = new Certification(master2, "CourseB", "CERT-005", "Desc2", "Org2", LocalDate.now());

        assertThrows(IllegalStateException.class, () -> master1.addCertification(cert2));
        assertSame(cert1, master1.getCertificationByNumber("CERT-005"));
    }

    @Test
    void addCertificationBelongingToAnotherMasterThrowsAndChangesNothing() {
        Certification cert = new Certification(master2, "Course", "CERT-006", "Desc", "Org", LocalDate.now());

        assertThrows(IllegalStateException.class, () -> master1.addCertification(cert));
        assertNull(master1.getCertificationByNumber("CERT-006"));
        assertSame(master2, cert.getMaster());
    }

    @Test
    void addDuplicateCertificationDoesNotDuplicate() {
        Certification cert = new Certification(master1, "Course", "CERT-007", "Desc", "Org", LocalDate.now());

        master1.addCertification(cert);

        assertEquals(cert, master1.getCertificationByNumber("CERT-007"));
        assertEquals(1, Certification.getCertificationList().stream().filter(c -> c.getCertificationNumber().equals("CERT-007")).count());
    }

    @Test
    void getCertificationByNumberReturnsCorrectCertification() {
        Certification cert = new Certification(master1, "Course", "CERT-008", "Desc", "Org", LocalDate.now());

        assertSame(cert, master1.getCertificationByNumber("CERT-008"));
    }

    @Test
    void getCertificationByNumberReturnsNullWhenNotFound() {
        assertNull(master1.getCertificationByNumber("UNKNOWN"));
    }

    @Test
    void getCertificationByNumberNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> master1.getCertificationByNumber(null));
    }

    @Test
    void getCertificationListReturnsCopy() {
        Certification cert = new Certification(master1, "Course", "CERT-020", "Desc", "Org", LocalDate.now());

        var list = master1.getCertificationList();

        list.clear();

        assertFalse(master1.getCertificationList().isEmpty());
        assertTrue(master1.getCertificationList().contains(cert));
    }



    @Test
    void removeCertificationWithNullDoesNothing() {
        Certification cert = new Certification(master1, "Course", "CERT-009", "Desc", "Org", LocalDate.now());

        master1.removeCertification(null);

        assertSame(cert, master1.getCertificationByNumber("CERT-009"));
        assertTrue(Certification.getCertificationList().contains(cert));
    }

    @Test
    void removeCertificationWithUnknownDoesNothing() {
        Certification cert = new Certification(master1, "Course", "CERT-010", "Desc", "Org", LocalDate.now());

        master1.removeCertification("XXX");

        assertSame(cert, master1.getCertificationByNumber("CERT-010"));
        assertTrue(Certification.getCertificationList().contains(cert));
    }

    @Test
    void removeCertificationProperlyRemovesFromExtent() {
        Certification cert = new Certification(master1, "Course", "CERT-011", "Desc", "Org", LocalDate.now());

        master1.removeCertification("CERT-011");

        assertNull(master1.getCertificationByNumber("CERT-011"));
        assertFalse(Certification.getCertificationList().contains(cert));
    }

    @Test
    void removingMasterDeletesAllCertificationsBecauseComposition() {
        Certification cert1 = new Certification(master1, "CourseA", "CERT-012", "Desc", "Org", LocalDate.now());
        Certification cert2 = new Certification(master1, "CourseB", "CERT-013", "Desc", "Org", LocalDate.now());

        master2.addServiceSpecialisesIn(master1.getDummyService());
        master1.removeMaster();

        assertNull(master1.getCertificationByNumber("CERT-012"));
        assertNull(master1.getCertificationByNumber("CERT-013"));
        assertFalse(Certification.getCertificationList().contains(cert1));
        assertFalse(Certification.getCertificationList().contains(cert2));
    }

    @Test
    void setMasterMovesCertificationAndPreservesQualifiedKey() {
        Certification cert = new Certification(master1, "Course", "CERT-014", "Desc", "Org", LocalDate.now());

        cert.setMaster(master2);

        assertNull(master1.getCertificationByNumber("CERT-014"));
        assertSame(cert, master2.getCertificationByNumber("CERT-014"));
        assertSame(master2, cert.getMaster());
    }

    @Test
    void setMasterNullThrowsException() {
        Certification cert = new Certification(master1, "Course", "CERT-015", "Desc", "Org", LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> cert.setMaster(null));
    }
}

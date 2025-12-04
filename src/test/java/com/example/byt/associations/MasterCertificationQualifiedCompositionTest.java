package com.example.byt.associations;

import com.example.byt.models.Certification;
import com.example.byt.models.person.Master;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MasterCertificationQualifiedCompositionTest {

    private Master master;
    private Master anotherMaster;

    @BeforeEach
    void setUp() {
        Master.clearExtent();
        Certification.clearExtent();
        master = new Master("Yelizaveta", "Gaiduk", "+48555111222", LocalDate.of(2005, 11, 15), 5);
        anotherMaster = new Master("Dana", "Nazarchuk", "+48550111222", LocalDate.of(1990, 1, 1), 10);
    }

    @Test
    void addCertificationAddsQualifiedAssociationAndReverseConnection() {
        Certification cert = new Certification("Brow Lamination", "CERT-001", "Brow lamination basic", "NoOffence Academy", LocalDate.now());
        master.addCertification(cert);
        assertSame(cert, master.getCertificationByNumber("CERT-001"));
        assertSame(master, cert.getMaster());
        assertTrue(Certification.getCertificationList().contains(cert));
    }

    @Test
    void certificationConstructorWithMasterCreatesCompositionAndQualifiedEntry() {
        Certification cert = new Certification(master, "Brow Lamination", "CERT-002", "Advanced course", "NoOffence Academy", LocalDate.now());

        assertSame(master, cert.getMaster());
        assertSame(cert, master.getCertificationByNumber("CERT-002"));
        assertTrue(Certification.getCertificationList().contains(cert));
    }

    @Test
    void getCertificationByNumberReturnsNullForUnknownNumber() {
        Certification cert = new Certification(master, "Brow lamination", "CERT-003", "Course", "NoOffence Academy", LocalDate.now());

        assertSame(cert, master.getCertificationByNumber("CERT-003"));
        assertNull(master.getCertificationByNumber("UNKNOWN"));
    }

    @Test
    void getCertificationByNumberWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class,() -> master.getCertificationByNumber(null));
    }

    @Test
    void addNullCertificationThrowsExceptionAndChangesNothing() {
        assertThrows(IllegalArgumentException.class, () -> master.addCertification(null));

        assertNull(master.getCertificationByNumber("ANY"));
        assertTrue(Certification.getCertificationList().isEmpty());
    }

    @Test
    void addCertificationWithBlankNumberThrowsException() {
        Certification cert = new Certification("Brow lamination", "", "Course", "NoOffence Academy", LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> master.addCertification(cert));
        assertNull(master.getCertificationByNumber(""));
    }

    @Test
    void addingSameCertificationInstanceTwiceDoesNotDuplicateQualifiedEntry() {
        Certification cert = new Certification(master, "Brow lamination", "CERT-004", "Course", "NoOffence Academy", LocalDate.now()
        );

        master.addCertification(cert);
        master.addCertification(cert);

        assertSame(cert, master.getCertificationByNumber("CERT-004"));
        assertEquals(1, Certification.getCertificationList().size());
    }

    @Test
    void addingCertificationWithExistingNumberButDifferentInstanceThrowsAndKeepsOriginal() {
        Certification cert1 = new Certification(master, "Brow lamination", "CERT-005", "Course v1", "NoOffence Academy", LocalDate.now());
        Certification cert2 = new Certification("Brow lamination", "CERT-005", "Course v2", "NoOffence Academy", LocalDate.now());

        assertThrows(IllegalStateException.class, () -> master.addCertification(cert2));
        assertSame(cert1, master.getCertificationByNumber("CERT-005"));
        assertNotSame(cert2, master.getCertificationByNumber("CERT-005"));
        assertEquals(2, Certification.getCertificationList().size());
    }

    @Test
    void addingCertificationBelongingToAnotherMasterThrowsAndDoesNotChangeBinding() {
        Certification cert = new Certification(anotherMaster, "Brow lamination", "CERT-006", "Course", "NoOffence Academy", LocalDate.now());

        assertThrows(IllegalStateException.class, () -> master.addCertification(cert));
        assertNull(master.getCertificationByNumber("CERT-006"));
        assertSame(anotherMaster, cert.getMaster());
        assertSame(cert, anotherMaster.getCertificationByNumber("CERT-006"));
    }

    @Test
    void removeCertificationRemovesQualifiedEntryReverseConnectionAndPartFromExtent() {
        Certification cert = new Certification(master, "Brow lamination", "CERT-007", "Course", "NoOffence Academy", LocalDate.now());

        assertSame(cert, master.getCertificationByNumber("CERT-007"));
        assertTrue(Certification.getCertificationList().contains(cert));
        master.removeCertification("CERT-007");

        assertNull(master.getCertificationByNumber("CERT-007"));
        assertFalse(Certification.getCertificationList().contains(cert));
        assertNull(cert.getMaster());
    }

    @Test
    void removeCertificationWithNullDoesNothing() {
        Certification cert = new Certification(master, "Brow lamination", "CERT-008", "Course", "NoOffence Academy", LocalDate.now());

        master.removeCertification(null);
        assertSame(cert, master.getCertificationByNumber("CERT-008"));
        assertTrue(Certification.getCertificationList().contains(cert));
        assertSame(master, cert.getMaster());
    }

    @Test
    void removeCertificationWithUnknownNumberDoesNothing() {
        Certification cert = new Certification(master, "Brow lamination", "CERT-009", "Course", "NoOffence Academy", LocalDate.now());

        master.removeCertification("UNKNOWN");

        assertSame(cert, master.getCertificationByNumber("CERT-009"));
        assertTrue(Certification.getCertificationList().contains(cert));
        assertSame(master, cert.getMaster());
    }

    @Test
    void removeMasterDeletesAllOwnedCertificationsFromExtentAndBreaksAssociations() {
        Certification cert1 = new Certification(master, "Brow lamination", "CERT-010", "Course 1", "NoOffence Academy", LocalDate.now());
        Certification cert2 = new Certification(master, "Brow lamination", "CERT-011", "Course 2", "NoOffence Academy", LocalDate.now());

        assertTrue(Certification.getCertificationList().contains(cert1));
        assertTrue(Certification.getCertificationList().contains(cert2));

        master.removeMaster();

        assertFalse(Certification.getCertificationList().contains(cert1));
        assertFalse(Certification.getCertificationList().contains(cert2));
        assertNull(cert1.getMaster());
        assertNull(cert2.getMaster());
        assertNull(master.getCertificationByNumber("CERT-010"));
        assertNull(master.getCertificationByNumber("CERT-011"));
        assertFalse(Master.getMasterList().contains(master));
    }

}

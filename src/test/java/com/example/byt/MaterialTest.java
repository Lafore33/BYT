package com.example.byt;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MaterialTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructorSetsCorrectValues() {
        String name = "Nail Polish";
        String producer = "OPI";
        Material material = new Material(name ,producer);
        assertEquals(name, material.getName(), "Incorrect name set in the constructor");
        assertEquals(producer, material.getProducer(), "Incorrect producer set in the constructor");
    }

    @Test
    void validMaterialShouldHaveNoViolations() {
        Material material = new Material("Nail Polish", "OPI");

        Set<ConstraintViolation<Material>> violations = validator.validate(material);

        assertTrue(violations.isEmpty(),
                "Expected no validation violations for a valid Material, but got: " + violations);
        List<Material> materialList = Material.getMaterialList();
        assertTrue(materialList.contains(material), "The valid material should be added to the list");
    }

    @Test
    void blankNameShouldFailValidation() {
        Material material = new Material(" ", "OPI");

        Set<ConstraintViolation<Material>> violations = validator.validate(material);

        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for blank 'name', but got: " + violations);
        List<Material> materialList = Material.getMaterialList();
        assertFalse(materialList.contains(material), "The invalid material should be added to the list");
    }

    @Test
    void nullNameShouldFailValidation() {
        Material material = new Material(null, "OPI");

        Set<ConstraintViolation<Material>> violations = validator.validate(material);

        assertTrue(containsViolationFor(violations, "name"),
                "Expected violation for null 'name', but got: " + violations);
        List<Material> materialList = Material.getMaterialList();
        assertFalse(materialList.contains(material), "The invalid material should be added to the list");
    }

    @Test
    void blankProducerShouldFailValidation() {
        Material material = new Material("Nail Polish", " ");

        Set<ConstraintViolation<Material>> violations = validator.validate(material);

        assertTrue(containsViolationFor(violations, "producer"),
                "Expected violation for blank 'producer', but got: " + violations);
        List<Material> materialList = Material.getMaterialList();
        assertFalse(materialList.contains(material), "The invalid material should be added to the list");
    }

    @Test
    void nullProducerShouldFailValidation() {
        Material material = new Material("Nail Polish", null);

        Set<ConstraintViolation<Material>> violations = validator.validate(material);

        assertTrue(containsViolationFor(violations, "producer"),
                "Expected violation for null 'producer', but got: " + violations);
        List<Material> materialList = Material.getMaterialList();
        assertFalse(materialList.contains(material), "The invalid material should be added to the list");
    }

    @Test
    void getMaterialListShouldReturnCopy(){
        Material material = new Material("Nail Polish", "OPI");
        List<Material> listCopy = Material.getMaterialList();
        listCopy.clear();

        List<Material> originalList = Material.getMaterialList();
        assertTrue(originalList.contains(material), "The original list should not be modified");
    }

    private boolean containsViolationFor(Set<ConstraintViolation<Material>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}

package com.example.byt.models.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SkinService extends Service implements Serializable {

    @NotBlank
    private String purpose;

    private static List<SkinService> skinServiceList = new ArrayList<>();
    private static final String EXTENT_FILE = "skinservice.ser";

    public SkinService(int id, String name, double regularPrice, String description,
                       double duration, String purpose) {
        super(id, name, regularPrice, description, duration);
        this.purpose = purpose;
        addSkinService(this);
    }

    private static void addSkinService(SkinService skinService){
        if (skinService == null){
            throw new NullPointerException("SkinService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SkinService>> violations = validator.validate(skinService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the service cannot be added to the list");
            return;
        }
        skinServiceList.add(skinService);
    }

    public static void save(){
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(EXTENT_FILE))) {
            oos.writeObject(skinServiceList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // added default constructor for proper deserialization
    // made it private on purpose
    private SkinService() {
    }

    public static void loadExtent() throws IOException, ClassNotFoundException {
        File file = new File(EXTENT_FILE);

        if (!file.exists()) {
            throw new FileNotFoundException("Extent file not found: " + EXTENT_FILE);
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            Object loadedObject = ois.readObject();

            if (!(loadedObject instanceof ArrayList<?>)) {
                throw new ClassCastException("Loaded object is not an ArrayList");
            }

            ArrayList<SkinService> loadedList = (ArrayList<SkinService>) loadedObject;

            for (Object obj : loadedList) {
                if (!(obj instanceof SkinService)) {
                    throw new ClassCastException("Loaded list contains non-SkinService objects");
                }
            }

            skinServiceList = loadedList;
        }
    }

    public static List<SkinService> getSkinServiceList() {
        return new ArrayList<>(skinServiceList);
    }

    public static void clearExtent() {
        skinServiceList.clear();
    }

    public static String getExtentFile() {
        return EXTENT_FILE;
    }

    public String getPurpose() {
        return purpose;
    }
}
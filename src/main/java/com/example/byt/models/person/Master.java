package com.example.byt.models.person;

import com.example.byt.models.Certification;
import com.example.byt.models.ProvidedService;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.*;

public class Master extends Worker {

    @Min(0)
    private int experience;

    private final static int minExperienceForTop = 3;

    private Set<Service> servicesSpecialisesIn = new HashSet<>();

    private static List<Master> masters = new ArrayList<>();

    private Master manager;
    private Set<Master> trainees = new HashSet<>();

    private Map<String, Certification> certificationsByNumber = new LinkedHashMap<>();

    private Set<ProvidedService> completedServices = new HashSet<>();

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate, int experience) {
        super(name, surname, phoneNumber, birthDate);
        this.experience = experience;
        addMaster(this);
        Service dummyService = new Service(0, "Dummy Service", 0, "This is a dummy service", 0, Set.of(this));
        Certification dummyCertification = new Certification(this, "Dummy", "0", "This is a dummy certification", "Dummy", LocalDate.now());
    }

    public Master(String name, String surname, String phoneNumber, LocalDate birthDate,
                  int experience, Set<Service> servicesSpecialisesIn) {
        super(name, surname, phoneNumber, birthDate);
        if (servicesSpecialisesIn == null || servicesSpecialisesIn.isEmpty()) {
            throw new IllegalArgumentException("Master should specialise in at least one service");
        }
        this.experience = experience;
        addMaster(this);
        for (Service service : servicesSpecialisesIn) {
            addServiceSpecialisesIn(service);
        }
    }

    private static void addMaster(Master master){
        if (master == null){
            throw new NullPointerException("Master cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Master>> violations = validator.validate(master);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the master cannot be added to the list");
            return;
        }
        masters.add(master);
    }

    public void removeMaster(){
        if (this.manager != null) {
            setManager(null);
        }
        for (Master trainee : new HashSet<>(this.trainees)) {
            removeTrainee(trainee);
        }
        for (Service service : new HashSet<>(servicesSpecialisesIn)) {
            servicesSpecialisesIn.remove(service);
            service.removeMasterSpecializedInForRemoval(this);
        }
        for (Certification certification : new ArrayList<>(certificationsByNumber.values())) {
            removeCertification(certification.getCertificationNumber());
        }
        masters.remove(this);
    }

    public void addCompletedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new NullPointerException("ProvidedService cannot be null");
        }
        if (completedServices.contains(providedService)) {
            return;
        }
        completedServices.add(providedService);
        if (!providedService.getCompletedByMasters().contains(this)) {
            providedService.addMaster(this);
        }
    }

    public void removeCompletedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new NullPointerException("ProvidedService cannot be null");
        }
        if (!completedServices.contains(providedService)) {
            return;
        }
        completedServices.remove(providedService);
        if (providedService.getCompletedByMasters().contains(this)) {
            providedService.removeMaster(this);
        }
    }

    public void setManager(Master newManager) {
        if (newManager == this) {
            throw new IllegalArgumentException("A master cannot manage themselves");
        }
        if (newManager != null && !newManager.isTopMaster()) {
            throw new IllegalStateException(
                    "Only top masters (experience >= " + minExperienceForTop + ") can manage trainees."
            );
        }
        if (this.manager == newManager) {
            return;
        }
        if (this.manager != null) {
            Master oldManager = this.manager;
            this.manager = null;
            oldManager.removeTrainee(this);
        }
        if (newManager == null) {
            this.manager = null;
            return;
        }
        this.manager = newManager;
        newManager.addTrainee(this);
    }

    public void addTrainee(Master trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee cannot be null.");
        }
        if (trainee == this) {
            throw new IllegalArgumentException("A master cannot be their own trainee.");
        }
        if (!this.isTopMaster()) {
            throw new IllegalStateException("Only top masters (experience >= " + minExperienceForTop + ") can manage trainees.");
        }
        if (this.trainees.contains(trainee)) {
            return;
        }
        if (trainee.manager != null && trainee.manager != this) {
            throw new IllegalStateException(
                    "This master already has a different manager. Remove current manager first."
            );
        }
        this.trainees.add(trainee);
        if (trainee.getManager() != this) {
            trainee.setManager(this);
        }
    }

    public void removeTrainee(Master trainee) {
        if (trainee == null || !this.trainees.contains(trainee)) {
            return;
        }
        this.trainees.remove(trainee);
        if (trainee.getManager() == this) {
            trainee.setManager(null);
        }
    }

    public void addServiceSpecialisesIn(Service service){
        if(service == null)
            throw new IllegalArgumentException("Service cannot be null");
        if(existsDummyService()){
            Service dummy = getDummyService();
            if(dummy != null) {
                servicesSpecialisesIn.remove(dummy);
                dummy.removeFromExtent();
            }
        }
        if(servicesSpecialisesIn.add(service))
            service.addMasterSpecializedIn(this);
    }

    public void removeServiceSpecialisesIn(Service service){
        if(service == null) return;
        if(servicesSpecialisesIn.remove(service))
            service.removeMasterSpecializedIn(this);
        if(servicesSpecialisesIn.isEmpty()) {
            addServiceSpecialisesIn(service);
            throw new IllegalStateException("Master should specialise in at least one service");
        }
    }

    public Set<Service> getServiceSpecialisesIn(){
        return new HashSet<>(servicesSpecialisesIn);
    }

    public void addCertification(Certification certification) {
        if (certification == null) {
            throw new IllegalArgumentException("Certification cannot be null.");
        }
        String number = certification.getCertificationNumber();

        if (certificationsByNumber.containsValue(certification)) {
            return;
        }
        Certification existing = certificationsByNumber.get(number);
        if (existing != null && existing != certification) {
            throw new IllegalStateException("Certification number already used for this Master.");
        }
        if (certification.getMaster() != null && certification.getMaster() != this) {
            throw new IllegalStateException("Certification belongs to another Master.");
        }

        if(existsDummyCertification()) {
            Certification dummy = getDummyCertification();
            if(dummy != null) {
                removeCertification(dummy.getCertificationNumber());
            }
        }
        certificationsByNumber.put(number, certification);
        if (certification.getMaster() != this) {
            certification.setMaster(this);
        }

    }


    public void removeCertification(String certNumber) {
        if (certNumber == null) {
            return;
        }
        Certification certification = certificationsByNumber.get(certNumber);
        if (certification == null) {
            return;
        }
        certificationsByNumber.remove(certNumber);
        certification.removeFromExtent();
    }

    public Certification getCertificationByNumber(String certificationNumber) {
        if (certificationNumber == null) {
            throw new IllegalArgumentException("certificationNumber cannot be null");
        }
        return certificationsByNumber.get(certificationNumber);
    }

    public boolean isTopMaster() {
        return this.experience >= minExperienceForTop;
    }

    public static int getMinExperienceForTop(){
        return minExperienceForTop;
    }

    public Master getManager() {
        return manager;
    }

    public Set<Master> getTrainees() {
        return new HashSet<>(trainees);
    }

    public boolean hasManager() {
        return this.manager != null;
    }

    public boolean hasTrainees() {
        return !this.trainees.isEmpty();
    }

    public int getTraineeCount() {
        return this.trainees.size();
    }

    public boolean isTrainee(Master master) {
        return this.trainees.contains(master);
    }

    public static List<Master> getMasterList() {
        return new ArrayList<>(masters);
    }

    public Set<ProvidedService> getCompletedServices() {
        return new HashSet<>(completedServices);
    }

    public boolean hasCompletedService(ProvidedService providedService) {
        return completedServices.contains(providedService);
    }

    public int getCompletedServiceCount() {
        return completedServices.size();
    }

    public int getExperience() {
        return experience;
    }

    public static void clearExtent() {
        masters.clear();
    }
    public Service getDummyService(){
        return servicesSpecialisesIn.stream()
                .filter(s -> s.getId() == 0 && "Dummy Service".equals(s.getName()))
                .findFirst()
                .orElse(null);
    }
    public boolean existsDummyService() {
        return servicesSpecialisesIn.stream().anyMatch(s ->
                s.getId() == 0 && s.getName().equals("Dummy Service")
        );
    }

    public boolean existsDummyCertification() {
        return certificationsByNumber.values().stream()
                .anyMatch(cert -> "0".equals(cert.getCertificationNumber())
                        && "Dummy".equals(cert.getName()));
    }

    public Certification getDummyCertification() {
        return certificationsByNumber.values().stream()
                .filter(cert -> "0".equals(cert.getCertificationNumber())
                        && "Dummy".equals(cert.getName()))
                .findFirst()
                .orElse(null);
    }

}
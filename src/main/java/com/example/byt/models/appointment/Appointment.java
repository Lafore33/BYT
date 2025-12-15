package com.example.byt.models.appointment;

import com.example.byt.models.AppointmentStatus;
import com.example.byt.models.HistoryOfStatus;
import com.example.byt.models.ProvidedService;
import com.example.byt.models.ServiceInfo;
import com.example.byt.models.person.Customer;
import com.example.byt.models.person.Receptionist;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Appointment implements Serializable {
    @NotNull
    private LocalDate date;

    private List<String> notes;

    private PaymentMethod paymentMethod;

    private Receptionist receptionist;

    private Set<ProvidedService> providedServices = new HashSet<>();

    private Set<HistoryOfStatus> historyOfStatuses = new HashSet<>();

    private static List<Appointment> appointments = new ArrayList<>();

    private Appointment() {
    }

    public Appointment(Builder builder) {
        this.date = builder.date;
        this.notes = builder.notes;
        this.paymentMethod = builder.paymentMethod;

        builder.services.forEach(this::addService);
        addCustomer(builder.customer);
        addReceptionist(builder.receptionist);

        addAppointment(this);
    }

    private static void addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new NullPointerException("Appointment cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the appointment cannot be added to the list");
            return;
        }
        appointments.add(appointment);
    }

    public void addService(ServiceInfo serviceInfo) {
        if (serviceInfo.getService() == null) {
            throw new NullPointerException("Service cannot be null");
        }
        if (serviceInfo.getMasters() == null || serviceInfo.getMasters().isEmpty()) {
            throw new IllegalArgumentException("ServiceInfo must include at least one master");
        }

        ProvidedService providedService = new ProvidedService.Builder(
                serviceInfo.getTime(),
                serviceInfo.getService(),
                this,
                serviceInfo.getMasters()
        ).build();
    }

    public void addProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new NullPointerException("ProvidedService cannot be null");
        }

        if (providedServices.contains(providedService)) {
            return;
        }

        providedServices.add(providedService);
        providedService.addAppointment(this);
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("Customer cannot be null");
        }

        HistoryOfStatus historyOfStatus = new HistoryOfStatus(AppointmentStatus.SCHEDULED, LocalDate.now(), customer, this);
    }

    public void addHistory(HistoryOfStatus historyOfStatus) {
        if (historyOfStatus == null) {
            throw new NullPointerException("HistoryOfStatus cannot be null");
        }

        if (this.historyOfStatuses.contains(historyOfStatus)) {
            return;
        }

        this.historyOfStatuses.add(historyOfStatus);
        historyOfStatus.addAppointment(this);
    }

    public void removeProvidedService(ProvidedService providedService) {
        if (providedService == null) {
            throw new NullPointerException("Provided service cannot be null");
        }

        if (!providedServices.contains(providedService)) {
            return;
        }

        providedServices.remove(providedService);
        providedService.removeAppointment(this);
    }

    public Receptionist getReceptionist() {
        return receptionist;
    }

    public void addReceptionist(Receptionist receptionist) {
        if (this.receptionist == receptionist) {
            return;
        }
        if (this.receptionist != null) {
            throw new IllegalArgumentException("The appointment is already done by another receptionist");
        }

        this.receptionist = receptionist;
        receptionist.addAppointment(this);

    }

    public List<String> getNotes() {
        return notes == null ? null : new ArrayList<>(notes);
    }

    public void setNotes(List<String> notes) {
        if (notes == null) {
            this.notes = null;
            return;
        }
        if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
            throw new IllegalArgumentException("notes can't contain null or empty elements");
        }
        this.notes = new ArrayList<>(notes);
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Set<ProvidedService> getProvidedServices() {
        return new HashSet<>(providedServices);
    }

    public Set<HistoryOfStatus> getHistoryOfStatuses() {
        return new HashSet<>(historyOfStatuses);
    }

    public void removeReceptionist(Receptionist receptionist) {
        if (receptionist == null) {
            throw new NullPointerException("Receptionist cannot be null");
        }
        if (this.receptionist != receptionist) {
            return;
        }
        this.receptionist = null;
        receptionist.removeAppointment(this);
    }


    public static class Builder {
        @NotNull
        private LocalDate date;

        private Customer customer;

        private Set<ServiceInfo> services;

        private List<String> notes;

        private PaymentMethod paymentMethod;

        private Receptionist receptionist;

        public Builder(LocalDate date, Customer customer, Set<ServiceInfo> services) {
            this.date = date;
            this.customer = customer;
            if (services == null || services.isEmpty()) {
                throw new IllegalArgumentException("services can't be null or empty");
            }
            this.services = services;
        }

        public Builder notes(List<String> notes) {
            if (notes == null) {
                this.notes = null;
                return this;
            }
            if (notes.stream().anyMatch(n -> n == null || n.trim().isBlank())) {
                throw new IllegalArgumentException("notes can't contain null or empty elements");
            }
            this.notes = new ArrayList<>(notes);
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder receptionist(Receptionist receptionist) {
            this.receptionist = receptionist;
            return this;
        }

        public Appointment build(){
            return new Appointment(this);
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotalPrice() {
        return providedServices.stream()
                .mapToDouble(ProvidedService::getPrice)
                .sum();
    }

    public LocalDate getDate() {
        return date;
    }

    public static List<Appointment> getAppointmentList() {
        return new ArrayList<>(appointments);
    }

    public static void clearExtent() {
        appointments.clear();
    }
}

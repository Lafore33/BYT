package com.example.byt.models.appointment;

import com.example.byt.models.ProvidedService;
import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Appointment {
    @NotNull
    private LocalDate date;

    private List<String> notes;

    private PaymentMethod paymentMethod;

    @Min(0)
    private double totalPrice;

    private Set<ProvidedService> servicesDone = new HashSet<>();

    private static List<Appointment> appointments = new ArrayList<>();
    public Appointment(Builder builder) {
        this.date = builder.date;
        this.notes = builder.notes;
        this.paymentMethod = builder.paymentMethod;
        addAppointment(this);
        if (builder.serviceWithMasters != null && !builder.serviceWithMasters.isEmpty()) {
            LocalDateTime appointmentTime = date.atStartOfDay();
            for (Map.Entry<Service, Set<Master>> entry : builder.serviceWithMasters.entrySet()) {
                Service service = entry.getKey();
                Set<Master> masters = entry.getValue();
                for (Master master : masters) {
                    if (!master.getServiceSpecialisesIn().contains(service)) {
                        throw new IllegalArgumentException("Master " + master.getName() + " does not specialize in service " + service.getName());
                    }
                }

                new ProvidedService.Builder(this, service, masters, appointmentTime).build();
            }
        }
    }
    public Appointment(Appointment other) {
        this.date = other.date;
        this.notes = other.notes == null ? null : new ArrayList<>(other.notes);
        this.paymentMethod = other.paymentMethod;
        this.totalPrice = other.totalPrice;
        this.servicesDone = new HashSet<>(other.servicesDone);
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

    public void addProvidedServiceInternal(ProvidedService providedService) {
        if (providedService == null) {
            throw new IllegalArgumentException("ProvidedService cannot be null");
        }
        servicesDone.add(providedService);
    }

    public void removeProvidedServiceInternal(ProvidedService providedService) {
        if (providedService != null) {
            servicesDone.remove(providedService);
        }
    }

    public void addServiceDone(Service service, Set<Master> masters, LocalDateTime time) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        if (masters == null || masters.isEmpty()) {
            throw new IllegalArgumentException("Service must have at least one master");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        for (Master master : masters) {
            if (!master.getServiceSpecialisesIn().contains(service)) {
                throw new IllegalArgumentException("Master " + master.getName() + " does not specialize in service " + service.getName());
            }
        }

        for (ProvidedService ps : servicesDone) {
            if (ps.getServiceRefersTo().equals(service)) {
                throw new IllegalArgumentException("This service is already in this appointment");
            }
        }

        new ProvidedService.Builder(this, service, masters, time).build();
    }

    public void removeServiceDone(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        if (servicesDone.size() <= 1) {
            throw new IllegalStateException("Cannot remove service: Appointment must have at least one service");
        }

        ProvidedService toRemove = null;
        for (ProvidedService ps : servicesDone) {
            if (ps.getServiceRefersTo().equals(service)) {
                toRemove = ps;
                break;
            }
        }

        if (toRemove != null) {
            toRemove.removeProvidedService();
        }
    }

    public void removeAppointment() {
        for (ProvidedService ps : new HashSet<>(servicesDone)) {
            ps.removeProvidedService();
        }
        servicesDone.clear();
        appointments.remove(this);
    }

    public Set<ProvidedService> getServicesDone() {
        return new HashSet<>(servicesDone);
    }

    public double getTotalPrice() {
        double total = 0;
        for (ProvidedService ps : servicesDone) {
            total += ps.getPrice();
        }
        return total;
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

    public static class Builder {
        @NotNull
        private LocalDate date;
        private List<String> notes;
        private PaymentMethod paymentMethod;
        private Map<Service, Set<Master>> serviceWithMasters;

        public Builder(LocalDate date) {
            this.date = date;
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
        public Builder serviceWithMasters(Map<Service, Set<Master>> serviceWithMasters) {
            if (serviceWithMasters == null || serviceWithMasters.isEmpty()) {
                throw new IllegalArgumentException("Appointment must have at least one service");
            }
            this.serviceWithMasters = new HashMap<>(serviceWithMasters);
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
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
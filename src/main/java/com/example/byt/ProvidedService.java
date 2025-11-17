package com.example.byt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProvidedService {

    @Min(0)
    @Max(5)
    private Integer rating;

    private String comment;

    @NotNull
    private LocalDateTime time;

    @Min(0)
    private double price;

    private static List<ProvidedService> providedServiceList = new ArrayList<>();

    private ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;
        addProvidedService(this);
    }

    private static void addProvidedService(ProvidedService providedService){
        if (providedService == null) {
            throw new NullPointerException("ProvidedService cannot be null");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProvidedService>> violations = validator.validate(providedService);
        if (!violations.isEmpty()) {
            System.out.println("Validation failed, the provided service cannot be added to the list");
            return;
        }
        providedServiceList.add(providedService);
    }

    public static class Builder {
        @Min(0)
        @Max(5)
        private Integer rating;

        private String comment;

        @NotNull
        private LocalDateTime time;

        public Builder(LocalDateTime time){
            this.time = time;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            if (comment == null) {
                this.comment = null;
            } else if (comment.trim().isEmpty()) {
                this.comment = null;
            } else {
                this.comment = comment;
            }
            return this;
        }

        public ProvidedService build() {
            return new ProvidedService(this);
        }
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTime() {
        return time;
    }
}

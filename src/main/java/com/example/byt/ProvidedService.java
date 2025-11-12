package com.example.byt;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ProvidedService {

    @Min(0)
    @Max(5)
    private Integer rating;

    private String comment;

    @NotNull
    private LocalDateTime time;

    @Min(0)
    private double price;

    public ProvidedService(Builder builder) {
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.time = builder.time;
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
            this.comment = comment;
            return this;
        }

        public ProvidedService build() {
            return new ProvidedService(this);
        }
    }
}

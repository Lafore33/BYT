package com.example.byt;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NailService extends Service {
    @NotNull
    private NailServiceType type;

    private boolean isCareIncluded;

    private static List<NailService> nailServiceList = new ArrayList<>();

    public NailService(int id, String name, double regularPrice, String description,
                       double duration, NailServiceType type, boolean isCareIncluded) {
        super(id, name, regularPrice, description, duration);
        this.type = type;
        this.isCareIncluded = isCareIncluded;
        addNailService(this);
    }

    private static void addNailService(NailService nailService){
        if (nailService == null){
            throw new NullPointerException("NailService cannot be null");
        }
        nailServiceList.add(nailService);
    }

}

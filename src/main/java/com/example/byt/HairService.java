package com.example.byt;

import java.util.List;

public class HairService {
    private HairServiceType type;
    private List<String> hairTypes;

    public HairService(HairServiceType type, List<String> hairTypes) {
        this.type = type;
        this.hairTypes = hairTypes;
    }
}

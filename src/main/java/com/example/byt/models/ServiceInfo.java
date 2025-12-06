package com.example.byt.models;

import com.example.byt.models.person.Master;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ServiceInfo {
    private LocalDateTime time;
    private Set<Master> masters;

    public ServiceInfo(LocalDateTime time, Set<Master> masters) {
        this.time = time;
        this.masters = masters;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Set<Master> getMasters() {
        return new HashSet<>(masters);
    }
}

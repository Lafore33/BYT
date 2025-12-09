package com.example.byt.models;

import com.example.byt.models.person.Master;
import com.example.byt.models.services.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ServiceInfo {
    private LocalDateTime time;
    private Set<Master> masters;
    private Service service;

    public ServiceInfo(Service service, LocalDateTime time, Set<Master> masters) {
        if (masters == null || masters.isEmpty()) {
            throw new IllegalArgumentException("ServiceInfo must include at least one master");
        }
        this.service = service;
        this.time = time;
        this.masters = masters;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Set<Master> getMasters() {
        return new HashSet<>(masters);
    }

    public Service getService() {
        return service;
    }
}

package com.example.byt.models;

import com.example.byt.models.person.Master;
import com.example.byt.models.services.FourHandsService;
import com.example.byt.models.services.Service;
import com.example.byt.models.services.TwoHandsService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ServiceInfo {
    private LocalDateTime time;
    private Set<Master> masters;
    private Object service;

    public ServiceInfo(Object service, LocalDateTime time, Set<Master> masters) {
        if (masters == null || masters.isEmpty()) {
            throw new IllegalArgumentException("ServiceInfo must include at least one master");
        }
        if (!(service instanceof Service) && !(service instanceof FourHandsService) && !(service instanceof TwoHandsService)) {
            throw new IllegalArgumentException("service has invalid type");
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

    public Object getService() {
        return service;
    }
}

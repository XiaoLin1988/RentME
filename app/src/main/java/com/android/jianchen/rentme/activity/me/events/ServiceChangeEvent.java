package com.android.jianchen.rentme.activity.me.events;

import com.android.jianchen.rentme.model.rentme.ServiceModel;

/**
 * Created by emerald on 12/16/2017.
 */
public class ServiceChangeEvent {
    private ServiceModel service;
    private int type; // 0 : Delete, 1 : Create

    public ServiceChangeEvent(ServiceModel service, int type) {
        this.service = service;
        this.type = type;
    }

    public ServiceModel getResult() {
        return service;
    }
    public int getType() { return type; }
}

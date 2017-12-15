package com.android.jianchen.rentme.activity.me.events;

import com.android.jianchen.rentme.model.rentme.ProjectModel;
import com.android.jianchen.rentme.model.rentme.ServiceModel;

/**
 * Created by emerald on 12/10/2017.
 */
public class ServiceDeleteEvent {
    private ServiceModel service;

    public ServiceDeleteEvent(ServiceModel service) {
        this.service = service;
    }

    public ServiceModel getResult() {
        return service;
    }
}

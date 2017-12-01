package com.android.emerald.rentme.Models;

import java.util.List;

/**
 * Created by emerald on 6/23/2017.
 */
public class SkillServiceModel {
    private String skill;
    private List<ServiceModel> services;

    public SkillServiceModel() {

    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }
}

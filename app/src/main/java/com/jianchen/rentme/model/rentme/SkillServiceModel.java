package com.jianchen.rentme.model.rentme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by emerald on 6/23/2017.
 */
public class SkillServiceModel implements Serializable {
    private String skill;
    private ArrayList<ServiceModel> services;

    public SkillServiceModel() {

    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public ArrayList<ServiceModel> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceModel> services) {
        this.services = services;
    }
}

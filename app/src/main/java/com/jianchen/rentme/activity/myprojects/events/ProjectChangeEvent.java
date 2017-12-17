package com.jianchen.rentme.activity.myprojects.events;

import com.jianchen.rentme.model.rentme.ProjectModel;

/**
 * Created by emerald on 12/16/2017.
 */
public class ProjectChangeEvent {
    private ProjectModel project;
    private int type; // 0 : Delete, 1 : Create, 2 : Complete, 3 : Review

    public ProjectChangeEvent(ProjectModel project, int type) {
        this.project = project;
        this.type = type;
    }

    public ProjectModel getResult() {
        return project;
    }
    public int getType() { return type; }
}

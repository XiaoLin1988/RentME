package com.android.jianchen.rentme.activity.myprojects.events;

import com.android.jianchen.rentme.model.rentme.ProjectModel;

/**
 * Created by emerald on 12/10/2017.
 */
public class ProjectCompleteEvent {
    private ProjectModel project;

    public ProjectCompleteEvent(ProjectModel project) {
        this.project = project;
    }

    public ProjectModel getResult() {
        return project;
    }
}

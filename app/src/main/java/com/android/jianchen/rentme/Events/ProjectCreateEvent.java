package com.android.jianchen.rentme.Events;

import com.android.jianchen.rentme.Models.ProjectModel;

/**
 * Created by emerald on 12/10/2017.
 */
public class ProjectCreateEvent {
    private ProjectModel project;

    public ProjectCreateEvent(ProjectModel project) {
        this.project = project;
    }

    public ProjectModel getResult() {
        return project;
    }
}

package com.android.jianchen.rentme.Events;

import com.android.jianchen.rentme.Models.ProjectModel;

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

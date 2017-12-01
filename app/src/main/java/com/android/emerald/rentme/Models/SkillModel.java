package com.android.emerald.rentme.Models;

/**
 * Created by emerald on 6/10/2017.
 */
public class SkillModel {
    private int id;
    private String path;
    private String title;

    public SkillModel() {
    }

    public SkillModel(int id, String path, String title) {
        this.id = id;
        this.path = path;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

package com.android.emerald.rentme.Models;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectModel {
    private int id;
    private String name;
    private String description;
    private int timeframe;
    private int consumer_id;
    private int consumer_score;
    private String consumer_review;
    private int talent_id;
    private int talent_score;
    private String talent_review;
    private int status;
    private String skill;
    private String preview;

    public ProjectModel() {
    }

    public ProjectModel(int id, String name, String description, int timeframe, int consumer_id, int consumer_score, String consumer_review, int talent_id, int talent_score, String talent_review, int status, String skill, String preview) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeframe = timeframe;
        this.consumer_id = consumer_id;
        this.consumer_score = consumer_score;
        this.consumer_review = consumer_review;
        this.talent_id = talent_id;
        this.talent_score = talent_score;
        this.talent_review = talent_review;
        this.status = status;
        this.skill = skill;
        this.preview = preview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(int timeframe) {
        this.timeframe = timeframe;
    }

    public int getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(int consumer_id) {
        this.consumer_id = consumer_id;
    }

    public int getConsumer_score() {
        return consumer_score;
    }

    public void setConsumer_score(int consumer_score) {
        this.consumer_score = consumer_score;
    }

    public String getConsumer_review() {
        return consumer_review;
    }

    public void setConsumer_review(String consumer_review) {
        this.consumer_review = consumer_review;
    }

    public int getTalent_id() {
        return talent_id;
    }

    public void setTalent_id(int talent_id) {
        this.talent_id = talent_id;
    }

    public int getTalent_score() {
        return talent_score;
    }

    public void setTalent_score(int talent_score) {
        this.talent_score = talent_score;
    }

    public String getTalent_review() {
        return talent_review;
    }

    public void setTalent_review(String talent_review) {
        this.talent_review = talent_review;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}

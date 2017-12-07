package com.android.emerald.rentme.Models;

import java.io.Serializable;

/**
 * Created by emerald on 6/23/2017.
 */
public class ServiceModel implements Serializable {
    private int id;
    private String title;
    private int talent_id;
    private String preview;
    private String detail;
    private int balance;
    private int skill_id;
    private String skill_title;
    private String skill_preview;
    private int review_cnt;
    private double review_score;

    public ServiceModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getTalent_id() {
        return talent_id;
    }

    public void setTalent_id(int talent_id) {
        this.talent_id = talent_id;
    }

    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkill_preview() {
        return skill_preview;
    }

    public void setSkill_preview(String skill_preview) {
        this.skill_preview = skill_preview;
    }

    public String getSkill_title() {
        return skill_title;
    }

    public void setSkill_title(String skill_title) {
        this.skill_title = skill_title;
    }

    public int getReview_cnt() {
        return review_cnt;
    }

    public void setReview_cnt(int review_cnt) {
        this.review_cnt = review_cnt;
    }

    public double getReview_score() {
        return review_score;
    }

    public void setReview_score(double review_score) {
        this.review_score = review_score;
    }
}
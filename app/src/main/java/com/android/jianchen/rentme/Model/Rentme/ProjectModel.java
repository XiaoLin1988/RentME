package com.android.jianchen.rentme.model.rentme;

import java.io.Serializable;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectModel implements Serializable {
    private int pr_id;
    private int pr_stts;
    private String pr_ctime;
    private int sv_id;
    private String sv_title;
    private String sv_preview;
    private int sv_balance;
    private String sv_detail;
    private int talent_id;
    private int skill_id;

    public int getPr_id() {
        return pr_id;
    }

    public void setPr_id(int pr_id) {
        this.pr_id = pr_id;
    }

    public int getPr_stts() {
        return pr_stts;
    }

    public void setPr_stts(int pr_stts) {
        this.pr_stts = pr_stts;
    }

    public String getPr_ctime() {
        return pr_ctime;
    }

    public void setPr_ctime(String pr_ctime) {
        this.pr_ctime = pr_ctime;
    }

    public int getSv_id() {
        return sv_id;
    }

    public void setSv_id(int sv_id) {
        this.sv_id = sv_id;
    }

    public String getSv_title() {
        return sv_title;
    }

    public void setSv_title(String sv_title) {
        this.sv_title = sv_title;
    }

    public String getSv_preview() {
        return sv_preview;
    }

    public void setSv_preview(String sv_preview) {
        this.sv_preview = sv_preview;
    }

    public int getSv_balance() {
        return sv_balance;
    }

    public void setSv_balance(int sv_balance) {
        this.sv_balance = sv_balance;
    }

    public String getSv_detail() {
        return sv_detail;
    }

    public void setSv_detail(String sv_detail) {
        this.sv_detail = sv_detail;
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
}

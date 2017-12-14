package com.android.jianchen.rentme.model.rentme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by emerald on 12/7/2017.
 */
public class ReviewModel implements Serializable {
    private int id;
    private int rv_type; // 0 : service, 1 : review
    private int rv_fid;
    private String rv_content;
    private int rv_score;
    private int rv_usr_id;
    private String rv_ctime;
    private String user_name;
    private String user_avatar;
    private boolean rated;
    private int rate_count;
    private int review_count;
    private ArrayList<WebLinkModel> web_links;
    private ArrayList<String> videos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRv_type() {
        return rv_type;
    }

    public void setRv_type(int rv_type) {
        this.rv_type = rv_type;
    }

    public int getRv_fid() {
        return rv_fid;
    }

    public void setRv_fid(int rv_fid) {
        this.rv_fid = rv_fid;
    }

    public String getRv_content() {
        return rv_content;
    }

    public void setRv_content(String rv_content) {
        this.rv_content = rv_content;
    }

    public int getRv_score() {
        return rv_score;
    }

    public void setRv_score(int rv_score) {
        this.rv_score = rv_score;
    }

    public int getRv_usr_id() {
        return rv_usr_id;
    }

    public void setRv_usr_id(int rv_usr_id) {
        this.rv_usr_id = rv_usr_id;
    }

    public String getRv_ctime() {
        return rv_ctime;
    }

    public void setRv_ctime(String rv_ctime) {
        this.rv_ctime = rv_ctime;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public int getRate_count() {
        return rate_count;
    }

    public void setRate_count(int rate_count) {
        this.rate_count = rate_count;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public ArrayList<WebLinkModel> getWeb_links() {
        return web_links;
    }

    public void setWeb_links(ArrayList<WebLinkModel> web_links) {
        this.web_links = web_links;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }
}

package com.android.jianchen.rentme.Models;

import java.io.Serializable;

/**
 * Created by emerald on 5/30/2017.
 */
public class UserModel implements Serializable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;
    private double latitude;
    private double longitude;
    private int zipcode;
    private String description;
    private String skills;
    private String avatar;
    private String fbId;
    private String fbName;
    private String fbProfileUrl;
    private String ggId;
    private String ggName;
    private String ggProfileUrl;
    private String wxId;
    private String wxName;
    private String wxProfileUrl;
    private String ctime;
    private String utime;
    private int usr_df;
    private String coverImg;
    private String mainImg;
    private int earning;
    private double distance;



    private String loginMode; // fb, gg, email

    public UserModel() {

    }

    public UserModel(int id, String name, String email, String phone, String password, String address, double latidue, double longitude, int zipcode, String description, String skills, String avatar, String fbId, String fbName, String fbProfileUrl, String ggId, String ggName, String ggProfileUrl, String wxId, String wxName, String wxProfileUrl, double distance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.latitude = latidue;
        this.longitude = longitude;
        this.zipcode = zipcode;
        this.description = description;
        this.skills = skills;
        this.avatar = avatar;
        this.fbId = fbId;
        this.fbName = fbName;
        this.fbProfileUrl = fbProfileUrl;
        this.ggId = ggId;
        this.ggName = ggName;
        this.ggProfileUrl = ggProfileUrl;
        this.wxId = wxId;
        this.wxName = wxName;
        this.wxProfileUrl = wxProfileUrl;
        this.distance = distance;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getFbProfileUrl() {
        return fbProfileUrl;
    }

    public void setFbProfileUrl(String fbProfileUrl) {
        this.fbProfileUrl = fbProfileUrl;
    }

    public String getGgId() {
        return ggId;
    }

    public void setGgId(String ggId) {
        this.ggId = ggId;
    }

    public String getGgName() {
        return ggName;
    }

    public void setGgName(String ggName) {
        this.ggName = ggName;
    }

    public String getGgProfileUrl() {
        return ggProfileUrl;
    }

    public void setGgProfileUrl(String ggProfileUrl) {
        this.ggProfileUrl = ggProfileUrl;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getWxProfileUrl() {
        return wxProfileUrl;
    }

    public void setWxProfileUrl(String wxProfileUrl) {
        this.wxProfileUrl = wxProfileUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public int getUsr_df() {
        return usr_df;
    }

    public void setUsr_df(int usr_df) {
        this.usr_df = usr_df;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public int getEarning() {
        return earning;
    }

    public void setEarning(int earning) {
        this.earning = earning;
    }

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

}

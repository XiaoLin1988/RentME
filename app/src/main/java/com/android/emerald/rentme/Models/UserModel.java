package com.android.emerald.rentme.Models;

import java.util.ArrayList;

/**
 * Created by emerald on 5/30/2017.
 */
public class UserModel {
    private int id;
    private int type;
    private String avatar;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;
    private double latidue;
    private double longitude;
    private int zipcode;
    private String description;
    private String workday;
    private int worktime;
    private double rate;
    private String skills;
    private double distance;

    public UserModel() {
        this.id = 0;
        this.type = 0;
        this.avatar = "";
        this.name = "";
        this.email = "";
        this.phone = "";
        this.password = "";
        this.address = "";
        this.latidue = 0;
        this.longitude = 0;
        this.zipcode = 0;
        this.description = "";
        this.workday = "[]";
        this.worktime = 0;
        this.rate = 0;
        this.skills = "[]";
        this.distance = 0;
    }

    public UserModel(int id, int type, String avatar, String name, String email, String phone, String password,
                     String address, double latidue, double longitude, int zipcode, String description,
                     String workday, int worktime, double rate, String skills, double distance) {
        this.id = id;
        this.type = type;
        this.avatar = avatar;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.latidue = latidue;
        this.longitude = longitude;
        this.zipcode = zipcode;
        this.description = description;
        this.workday = workday;
        this.worktime = worktime;
        this.rate = rate;
        this.skills = skills;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getLatidue() {
        return latidue;
    }

    public void setLatidue(double latidue) {
        this.latidue = latidue;
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

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public int getWorktime() {
        return worktime;
    }

    public void setWorktime(int worktime) {
        this.worktime = worktime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

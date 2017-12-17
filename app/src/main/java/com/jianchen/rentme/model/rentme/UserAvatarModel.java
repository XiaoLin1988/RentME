package com.jianchen.rentme.model.rentme;

import java.util.ArrayList;

/**
 * Created by emerald on 12/8/2017.
 */
public class UserAvatarModel {

    private ArrayList<String> subProfile;
    private ArrayList<String> mainProfile;

    public ArrayList<String> getMainProfile() {
        return mainProfile;
    }

    public void setMainProfile(ArrayList<String> mainProfile) {
        this.mainProfile = mainProfile;
    }

    public ArrayList<String> getSubProfile() {
        return subProfile;
    }

    public void setSubProfile(ArrayList<String> subProfile) {
        this.subProfile = subProfile;
    }




}

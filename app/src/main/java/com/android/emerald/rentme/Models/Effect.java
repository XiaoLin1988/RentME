package com.android.emerald.rentme.Models;

/**
 * Created by emerald on 2017/2/6.
 */
public class Effect {
    private int action;
    private int thumbId;
    private int selThumbId;
    private String name;

    public Effect(int action, int thumbId, int selThumbId, String name) {
        this.action = action;
        this.thumbId = thumbId;
        this.selThumbId = selThumbId;
        this.name = name;
    }

    public int getThumbId() {
        return thumbId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelThumbId() {
        return selThumbId;
    }

    public void setSelThumbId(int selThumbId) {
        this.selThumbId = selThumbId;
    }
}

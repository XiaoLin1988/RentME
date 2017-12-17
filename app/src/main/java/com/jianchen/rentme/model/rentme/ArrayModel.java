package com.jianchen.rentme.model.rentme;

import java.util.ArrayList;

/**
 * Created by emerald on 10/23/2017.
 */
public class ArrayModel<T> {
    private boolean status;
    private ArrayList<T> data;

    public ArrayModel() {
        status = false;
        data = new ArrayList<>();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}

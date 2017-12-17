package com.jianchen.rentme.model.rentme;

/**
 * Created by emerald on 10/23/2017.
 */
public class ObjectModel<T> {
    private boolean status;
    private T data;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

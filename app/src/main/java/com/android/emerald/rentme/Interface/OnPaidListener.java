package com.android.emerald.rentme.Interface;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by emerald on 7/16/2017.
 */
public interface OnPaidListener extends Serializable {
    public void onPaid(String detail);
}

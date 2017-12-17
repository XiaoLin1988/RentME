package com.jianchen.rentme.helper.delegator;

import android.view.View;

import com.jianchen.rentme.model.rentme.ServiceModel;

/**
 * Created by emerald on 12/6/2017.
 */
public interface OnServiceClickListener {
    void onServiceClick(View view, ServiceModel service);
}

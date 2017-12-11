package com.android.jianchen.rentme.Listener;

import android.app.Activity;

import com.android.jianchen.rentme.Interface.OnStepBackListener;

/**
 * Created by emerald on 6/9/2017.
 */
public class BaseBackPressListener implements OnStepBackListener {
    private Activity activity;

    public BaseBackPressListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onStepBack() {
        //activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}

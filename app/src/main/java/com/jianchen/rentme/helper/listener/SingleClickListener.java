package com.jianchen.rentme.helper.listener;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;

    private long mLastClickTime;

    public static boolean isViewClicked = false;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;

        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;
        if(!isViewClicked){
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }
        onSingleClick(v);
    }

    private void startTimer() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isViewClicked = false;
            }
        }, 300);

    }

}
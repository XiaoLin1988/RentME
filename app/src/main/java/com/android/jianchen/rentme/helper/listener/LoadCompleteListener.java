package com.android.jianchen.rentme.helper.listener;

/**
 * Created by emerald on 12/9/2017.
 */
public abstract class LoadCompleteListener {
    private int loaded;

    public abstract void onLoaded();

    public LoadCompleteListener(int loadCount) {
        loaded = loadCount - 1;
    }

    public void setLoaded() {
        if (loaded <= 0) {
            onLoaded();
        } else {
            loaded --;
        }
    }
}

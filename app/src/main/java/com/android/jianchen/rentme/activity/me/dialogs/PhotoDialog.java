package com.android.jianchen.rentme.activity.me.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import com.android.jianchen.rentme.R;

/**
 * Created by emerald on 12/8/2017.
 */
public class PhotoDialog extends Dialog {
    public PhotoDialog(Context context) {
        super(context);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_photo);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationScale;

        initDialog();
    }

    private void initDialog() {

    }
}

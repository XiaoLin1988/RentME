package com.android.emerald.rentme.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import com.android.emerald.rentme.R;

/**
 * Created by emerald on 12/8/2017.
 */
public class WebLinkDialog extends Dialog {
    public WebLinkDialog(Context context) {
        super(context);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_weblink);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationScale;

        initDialog();
    }

    private void initDialog() {

    }
}
